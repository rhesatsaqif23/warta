package com.rhesdev.warta.feature.news.presentation.search

import com.rhesdev.warta.feature.news.domain.model.News

data class SearchUiState(
    val query: String = "",
    val results: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
