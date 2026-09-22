package com.rhesdev.warta.feature.home.presentation.list

import com.rhesdev.warta.feature.news.domain.model.News

data class HomeUiState(
    val topNews: List<News> = emptyList(),
    val selectedCategory: String = "nasional",
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
) {
    val headlineNews: List<News> get() = topNews.take(3)
    val popularNews: News? get() = topNews.getOrNull(3)
    val trendingNews: List<News> get() = topNews.drop(4)
}
