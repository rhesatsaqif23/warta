package com.rhesdev.warta.feature.home.presentation.list

import com.rhesdev.warta.feature.news.domain.model.News

data class HomeUiState(
    val allNews: List<News> = emptyList(),
    val selectedCategory: String? = null,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
) {
    val filteredNews: List<News>
        get() = if (selectedCategory == null) {
            allNews
        } else {
            allNews.filter { it.category == selectedCategory }
        }

    val headlineNews: List<News> get() = filteredNews.take(3)
    val popularNews: News? get() = filteredNews.getOrNull(3)
    val trendingNews: List<News> get() = filteredNews.drop(4)
}
