package com.rhesdev.warta.feature.news.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rhesdev.warta.core.utils.toUserMessage
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.usecase.GetHomeFeedUseCase
import com.rhesdev.warta.feature.news.domain.usecase.GetTrendStatsUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsByCategoryUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsByDayUseCase
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsUseCase
import com.rhesdev.warta.feature.news.presentation.home.components.homeCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Home state holder: page stream from Room/RemoteMediator plus refresh + trend stats.
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeFeedUseCase: GetHomeFeedUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase,
    private val refreshNewsByCategoryUseCase: RefreshNewsByCategoryUseCase,
    private val refreshNewsByDayUseCase: RefreshNewsByDayUseCase,
    private val getTrendStatsUseCase: GetTrendStatsUseCase
) : ViewModel() {

    val homeFeed: Flow<PagingData<News>> = getHomeFeedUseCase().cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        refresh()
        loadTrendStats()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnCategorySelected -> selectCategory(event.category)
            is HomeUiEvent.OnDaySelected -> selectDay(event.day)
            HomeUiEvent.OnRetry -> refresh(isInitial = true)
            HomeUiEvent.OnRefresh -> refresh(isInitial = false)
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
                _uiState.update { it.copy(error = e.toUserMessage()) }
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
            silentFetch { refreshNewsUseCase() }
        }
    }

    private fun silentFetch(fetch: suspend () -> Unit) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(error = null) }
            try {
                fetch()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
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
}