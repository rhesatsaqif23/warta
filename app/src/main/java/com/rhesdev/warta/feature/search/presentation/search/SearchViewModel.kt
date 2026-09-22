package com.rhesdev.warta.feature.search.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.SearchNewsUseCase
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(300)
                _uiState.update { it.copy(isLoading = true) }
                try {
                    Log.d(TAG, "Searching API for: $query")
                    repository.searchAndRefresh(query)
                } catch (e: Exception) {
                    Log.e(TAG, "API search failed", e)
                }
                searchNewsUseCase(query)
                    .catch { e ->
                        _uiState.update { it.copy(error = e.message, isLoading = false) }
                    }
                    .collect { results ->
                        Log.d(TAG, "Search returned ${results.size} results")
                        _uiState.update { it.copy(results = results, isLoading = false) }
                    }
            }
        } else {
            _uiState.update { it.copy(results = emptyList(), isLoading = false) }
        }
    }
}
