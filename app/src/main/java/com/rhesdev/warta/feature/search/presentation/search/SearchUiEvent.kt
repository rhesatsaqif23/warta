package com.rhesdev.warta.feature.search.presentation.search

// One-way user actions for the Search screen.
sealed interface SearchUiEvent {
    data class OnQueryChanged(val query: String) : SearchUiEvent
    data object OnClearQuery : SearchUiEvent
}
