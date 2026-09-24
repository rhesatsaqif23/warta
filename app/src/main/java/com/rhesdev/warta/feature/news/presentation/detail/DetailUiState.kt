package com.rhesdev.warta.feature.news.presentation.detail

import com.rhesdev.warta.feature.news.domain.model.News

data class DetailUiState(
    val news: News? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
