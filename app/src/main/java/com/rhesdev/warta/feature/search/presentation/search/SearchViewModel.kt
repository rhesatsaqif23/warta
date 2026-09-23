package com.rhesdev.warta.feature.search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.SearchAndRefreshUseCase
import com.rhesdev.warta.feature.news.domain.usecase.SearchNewsUseCase
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

// Search state holder with debounced API-backed queries.
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val searchAndRefreshUseCase: SearchAndRefreshUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnQueryChanged -> submitQuery(event.query)
            SearchUiEvent.OnClearQuery -> submitQuery("")
        }
    }

    private fun submitQuery(query: String) {
        _uiState.update { it.copy(query = query, error = null) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), isLoading = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            _uiState.update { it.copy(isLoading = true) }
            try {
                searchAndRefreshUseCase(query)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
            searchNewsUseCase(query)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { results ->
                    _uiState.update {
                        it.copy(
                            results = results.filter { news -> news.imageUrl.isNotBlank() },
                            isLoading = false
                        )
                    }
                }
        }
    }
}
