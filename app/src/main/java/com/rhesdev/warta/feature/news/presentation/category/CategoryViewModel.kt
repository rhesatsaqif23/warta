package com.rhesdev.warta.feature.news.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.GetTopNewsUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsByCategoryUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsUseCase
import com.rhesdev.warta.feature.news.presentation.home.components.homeCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Category state holder warming the cache per chip keyword.
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase,
    private val refreshNewsByCategoryUseCase: RefreshNewsByCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    private var warmJob: Job? = null

    init {
        observeNews()
        warmCache()
    }

    fun onEvent(event: CategoryUiEvent) {
        when (event) {
            is CategoryUiEvent.OnToggleExpand -> {
                _uiState.update {
                    val expanded = if (event.key in it.expanded) {
                        it.expanded - event.key
                    } else {
                        it.expanded + event.key
                    }
                    it.copy(expanded = expanded)
                }
            }
            CategoryUiEvent.OnRetry -> warmCache(isInitial = true)
            CategoryUiEvent.OnRefresh -> warmCache(isInitial = false)
        }
    }

    private fun observeNews() {
        viewModelScope.launch {
            getTopNewsUseCase()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { news ->
                    _uiState.update { it.copy(allNews = news, isLoading = false) }
                }
        }
    }

    private fun warmCache(isInitial: Boolean = true) {
        warmJob?.cancel()
        warmJob = viewModelScope.launch {
            if (isInitial) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            } else {
                _uiState.update { it.copy(isRefreshing = true) }
            }
            try {
                refreshNewsUseCase()
                homeCategories.forEach { cat ->
                    val key = cat.key ?: return@forEach
                    val query = cat.query ?: return@forEach
                    refreshNewsByCategoryUseCase(query, key)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
            }
        }
    }
}
