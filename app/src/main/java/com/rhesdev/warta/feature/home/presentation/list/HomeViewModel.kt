package com.rhesdev.warta.feature.home.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.GetNewsByCategoryUseCase
import com.rhesdev.warta.feature.news.domain.usecase.GetTopNewsUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val getNewsByCategoryUseCase: GetNewsByCategoryUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadNews("nasional")
    }

    fun loadNews(category: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, selectedCategory = category) }
            getNewsByCategoryUseCase(category)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { news ->
                    _uiState.update { it.copy(topNews = news, isLoading = false) }
                }
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                refreshNewsUseCase("cnn-news", _uiState.value.selectedCategory)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
