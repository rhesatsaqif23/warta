package com.rhesdev.warta.feature.news.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.GetTopNewsUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Home state holder observing Room once and refreshing from the API.
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        observeNews()
        refresh()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnCategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
            }
            HomeUiEvent.OnRetry -> refresh(isInitial = true)
            HomeUiEvent.OnRefresh -> refresh(isInitial = false)
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

    private fun refresh(isInitial: Boolean = true) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            if (isInitial) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            } else {
                _uiState.update { it.copy(isRefreshing = true) }
            }
            try {
                refreshNewsUseCase()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
            }
        }
    }
}
