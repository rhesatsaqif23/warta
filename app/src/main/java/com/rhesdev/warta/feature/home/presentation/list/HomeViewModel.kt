package com.rhesdev.warta.feature.home.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "ViewModel initialized")
        loadNews()
    }

    fun loadNews(category: String? = null) {
        Log.d(TAG, "loadNews called, category=$category")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, selectedCategory = category) }
            try {
                Log.d(TAG, "Fetching from API")
                refreshNewsUseCase()
                Log.d(TAG, "API fetch complete")
            } catch (e: Exception) {
                Log.e(TAG, "API fetch failed, using cache", e)
            }
            getTopNewsUseCase()
                .catch { e ->
                    Log.e(TAG, "Error reading from Room", e)
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { news ->
                    Log.d(TAG, "Received ${news.size} total news items")
                    _uiState.update { it.copy(allNews = news, isLoading = false) }
                }
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                refreshNewsUseCase()
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
