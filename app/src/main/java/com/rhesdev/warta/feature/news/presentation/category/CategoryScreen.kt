package com.rhesdev.warta.feature.news.presentation.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.R
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.ErrorState
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.theme.Accent
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.core.utils.Dimens
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.presentation.components.SectionHeader
import com.rhesdev.warta.feature.news.presentation.components.TrendingNewsItem
import com.rhesdev.warta.feature.news.presentation.home.components.homeCategories

// Category screen grouping articles into expandable per-category sections.
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialCategory: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialCategory) {
        val key = initialCategory ?: return@LaunchedEffect
        if (homeCategories.any { it.key == key }) {
            viewModel.onEvent(CategoryUiEvent.OnToggleExpand(key))
        }
    }

    CategoryContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNewsClick = onNewsClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryContent(
    uiState: CategoryUiState,
    onEvent: (CategoryUiEvent) -> Unit,
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { onEvent(CategoryUiEvent.OnRefresh) },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = Dimens.smallPadding)
        ) {
        when {
            uiState.isLoading -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingScreen()
                }
            }
            uiState.error != null && uiState.sections.isEmpty() -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorState(
                        title = stringResource(R.string.error_news_load),
                        message = uiState.error ?: stringResource(R.string.error_generic),
                        onRetry = { onEvent(CategoryUiEvent.OnRetry) }
                    )
                }
            }
            uiState.sections.isEmpty() -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(
                        title = stringResource(R.string.empty_news),
                        message = stringResource(R.string.empty_news_all_categories)
                    )
                }
            }
            else -> CategorySections(
                uiState = uiState,
                onEvent = onEvent,
                onNewsClick = onNewsClick
            )
        }
        }
    }
}

private fun LazyListScope.CategorySections(
    uiState: CategoryUiState,
    onEvent: (CategoryUiEvent) -> Unit,
    onNewsClick: (String) -> Unit
) {
    uiState.sections.forEachIndexed { index, section ->
        item {
            SectionHeader(
                title = stringResource(section.labelRes),
                icon = Icons.Outlined.GridView,
                modifier = Modifier
                    .padding(horizontal = Dimens.smallPadding)
                    .padding(top = if (index == 0) 0.dp else Dimens.customPaddingLabel)
            )
        }
        items(section.articles, key = { it.link }) { news ->
            TrendingNewsItem(
                news = news,
                onClick = { onNewsClick(news.link) },
                modifier = Modifier.padding(horizontal = Dimens.smallPadding, vertical = Dimens.customSmallPaddingLabel)
            )
        }
        if (!section.expanded && section.total > section.articles.size) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.smallPadding, vertical = Dimens.xxxsPadding),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = { onEvent(CategoryUiEvent.OnToggleExpand(section.key)) }) {
                        Text(
                            text = stringResource(R.string.section_more),
                            color = Accent
                        )
                    }
                }
            }
        }
    }
}

private fun sampleNews(link: String, title: String, category: String) = News(
    link = link,
    title = title,
    contentSnippet = "Ringkasan berita.",
    isoDate = "2 jam lalu",
    imageUrl = "https://example.com/img.jpg",
    source = "Warta",
    category = category
)

private val sampleSections = listOf(
    sampleNews("https://example.com/1", "Timnas Lolos ke Final", "sports"),
    sampleNews("https://example.com/2", "Liga Selesai Digelar", "sports"),
    sampleNews("https://example.com/3", "Atlet Raih Emas", "sports"),
    sampleNews("https://example.com/4", "Saham Menguat", "economy"),
    sampleNews("https://example.com/5", "Rupiah Stabil", "economy")
)

@Preview(showBackground = true)
@Composable
private fun CategoryContentPreview() {
    WartaTheme {
        CategoryContent(
            uiState = CategoryUiState(
                allNews = sampleSections,
                isLoading = false
            ),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryContentLoadingPreview() {
    WartaTheme {
        CategoryContent(
            uiState = CategoryUiState(isLoading = true),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryContentEmptyPreview() {
    WartaTheme {
        CategoryContent(
            uiState = CategoryUiState(isLoading = false),
            onEvent = {},
            onNewsClick = {}
        )
    }
}
