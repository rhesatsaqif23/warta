package com.rhesdev.warta.feature.home.presentation.list

/** One-way user actions for the Home screen (UI -> ViewModel). */
sealed interface HomeUiEvent {
    data class OnCategorySelected(val category: String?) : HomeUiEvent
    data object OnRetry : HomeUiEvent
    data object OnRefresh : HomeUiEvent
}
