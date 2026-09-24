package com.rhesdev.warta.feature.news.presentation.home

// One-way user actions for the Home screen.
sealed interface HomeUiEvent {
    data class OnCategorySelected(val category: String?) : HomeUiEvent
    data class OnSourceSelected(val source: String?) : HomeUiEvent
    data class OnDaySelected(val day: String?) : HomeUiEvent
    data object OnRetry : HomeUiEvent
    data object OnRefresh : HomeUiEvent
    data object OnLoadMore : HomeUiEvent
}
