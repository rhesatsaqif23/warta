package com.rhesdev.warta.feature.news.presentation.category

import androidx.annotation.StringRes
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.presentation.home.components.homeCategories

// Category screen state with per-category article sections.
data class CategoryUiState(
    val allNews: List<News> = emptyList(),
    val expanded: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
) {
    data class Section(
        val key: String,
        @StringRes val labelRes: Int,
        val articles: List<News>,
        val total: Int,
        val expanded: Boolean
    )

    val sections: List<Section>
        get() = homeCategories.mapNotNull { cat ->
            val key = cat.key ?: return@mapNotNull null
            val list = allNews.filter { it.imageUrl.isNotBlank() && it.category == key }
            if (list.isEmpty()) null
            else Section(
                key = key,
                labelRes = cat.labelRes,
                articles = if (key in expanded) list else list.take(3),
                total = list.size,
                expanded = key in expanded
            )
        }
}
