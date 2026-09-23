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
        get() = allNews
            .filter { it.imageUrl.isNotBlank() }
            .let { withImage ->
                if (selectedCategory == null) {
                    withImage
                } else {
                    withImage.filter { it.category == selectedCategory }
                }
            }

    val headlineNews: List<News> get() = filteredNews.take(3)
    val popularNews: News? get() = filteredNews.getOrNull(3)
    val trendingNews: List<News> get() = filteredNews.drop(4)
}
