package com.rhesdev.warta.feature.news.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhesdev.warta.feature.news.domain.usecase.GetTopNewsUseCase
import com.rhesdev.warta.feature.news.domain.usecase.GetTrendStatsUseCase
import com.rhesdev.warta.feature.news.domain.usecase.LoadMoreNewsUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsByCategoryUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsByDayUseCase
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

private const val PAGE_SIZE = 100

// Home state holder observing Room once and refreshing from the API.
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopNewsUseCase: GetTopNewsUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase,
    private val refreshNewsByCategoryUseCase: RefreshNewsByCategoryUseCase,
    private val refreshNewsByDayUseCase: RefreshNewsByDayUseCase,
    private val loadMoreNewsUseCase: LoadMoreNewsUseCase,
    private val getTrendStatsUseCase: GetTrendStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var refreshJob: Job? = null
    private var latestOffset = PAGE_SIZE

    init {
        observeNews()
        refresh()
        loadTrendStats()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnCategorySelected -> selectCategory(event.category)
            is HomeUiEvent.OnDaySelected -> selectDay(event.day)
            HomeUiEvent.OnRetry -> refresh(isInitial = true)
            HomeUiEvent.OnRefresh -> refresh(isInitial = false)
            HomeUiEvent.OnLoadMore -> loadMore()
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
                latestOffset = PAGE_SIZE
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false, isRefreshing = false) }
            }
        }
    }

    private fun selectCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
        val query = homeCategories.firstOrNull { it.key == category }?.query
        if (query != null && category != null) {
            silentFetch { refreshNewsByCategoryUseCase(query, category) }
        } else {
            silentFetch {
                refreshNewsUseCase()
                latestOffset = PAGE_SIZE
            }
        }
    }

    private fun silentFetch(fetch: suspend () -> Unit) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(error = null) }
            try {
                fetch()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    private fun loadTrendStats() {
        viewModelScope.launch {
            val stats = getTrendStatsUseCase()
            _uiState.update { it.copy(trendByDay = stats.byDay) }
        }
    }

    private fun selectDay(day: String?) {
        _uiState.update { it.copy(selectedDay = day) }
        if (day == null) return
        silentFetch { refreshNewsByDayUseCase(day) }
    }

    private fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore) return
        if (state.selectedCategory != null || state.selectedDay != null) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            try {
                loadMoreNewsUseCase(latestOffset)
                latestOffset += PAGE_SIZE
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }
}
