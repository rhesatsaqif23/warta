package com.rhesdev.warta.feature.news.presentation.category

// One-way user actions for the Category screen.
sealed interface CategoryUiEvent {
    data class OnToggleExpand(val key: String) : CategoryUiEvent
    data object OnRetry : CategoryUiEvent
    data object OnRefresh : CategoryUiEvent
}
