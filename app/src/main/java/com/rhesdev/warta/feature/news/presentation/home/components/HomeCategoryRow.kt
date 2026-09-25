package com.rhesdev.warta.feature.news.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhesdev.warta.R
import com.rhesdev.warta.core.presentation.components.CategoryChip
import com.rhesdev.warta.core.presentation.theme.WartaTheme

// Home category filter model and chips row.
data class HomeCategory(val key: String?, @StringRes val labelRes: Int, val query: String?)

val homeCategories = listOf(
    HomeCategory(null, R.string.category_all, null),
    HomeCategory("society", R.string.category_national, "nasional"),
    HomeCategory("technology", R.string.category_technology, "teknologi"),
    HomeCategory("economy", R.string.category_economy, "ekonomi"),
    HomeCategory("sports", R.string.category_sports, "olahraga"),
    HomeCategory("entertainment", R.string.category_entertainment, "hiburan"),
    HomeCategory("politics", R.string.category_politics, "politik"),
    HomeCategory("health", R.string.category_health, "kesehatan")
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
        items(categories, key = { it.labelRes }) { category ->
            CategoryChip(
                label = stringResource(category.labelRes),
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