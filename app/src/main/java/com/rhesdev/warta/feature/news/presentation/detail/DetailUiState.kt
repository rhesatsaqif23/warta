package com.rhesdev.warta.feature.news.presentation.detail

import com.rhesdev.warta.feature.news.domain.model.News

// Detail UI state with lazily loaded full body text.
data class DetailUiState(
    val news: News? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val fullText: String? = null,
    val isLoadingBody: Boolean = false,
    val bodyError: String? = null,
    val bodyNotAvailable: Boolean = false
)
