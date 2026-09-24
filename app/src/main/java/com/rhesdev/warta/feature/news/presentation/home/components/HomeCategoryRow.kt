package com.rhesdev.warta.feature.news.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhesdev.warta.core.presentation.components.CategoryChip
import com.rhesdev.warta.core.presentation.theme.WartaTheme

// Home category filter model and chips row.
data class HomeCategory(val key: String?, val label: String, val query: String?)

val homeCategories = listOf(
    HomeCategory(null, "Semua", null),
    HomeCategory("society", "Nasional", "nasional"),
    HomeCategory("technology", "Teknologi", "teknologi"),
    HomeCategory("economy", "Ekonomi", "ekonomi"),
    HomeCategory("sports", "Olahraga", "olahraga"),
    HomeCategory("entertainment", "Hiburan", "hiburan"),
    HomeCategory("politics", "Politik", "politik"),
    HomeCategory("health", "Kesehatan", "kesehatan")
)

@Composable
fun HomeCategoryRow(
    categories: List<HomeCategory>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories, key = { it.label }) { category ->
            CategoryChip(
                label = category.label,
                selected = selectedCategory == category.key,
                onClick = { onCategorySelected(category.key) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeCategoryRowPreview() {
    WartaTheme {
        HomeCategoryRow(
            categories = homeCategories,
            selectedCategory = null,
            onCategorySelected = {}
        )
    }
}
