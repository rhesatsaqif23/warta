package com.rhesdev.warta.feature.news.presentation.home

// Home UI state for filters, refresh flags and trend stats; the feed is a PagingData stream.
data class HomeUiState(
    val selectedCategory: String? = null,
    val trendByDay: Map<String, Int> = emptyMap(),
    val selectedDay: String? = null,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)