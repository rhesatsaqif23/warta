package com.rhesdev.warta.feature.news.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.R
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.ErrorState
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.core.utils.Dimens
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.presentation.components.HeadlineCard
import com.rhesdev.warta.feature.news.presentation.components.PopularNewsCard
import com.rhesdev.warta.feature.news.presentation.components.SectionHeader
import com.rhesdev.warta.feature.news.presentation.components.TrendingNewsItem
import com.rhesdev.warta.feature.news.presentation.home.components.HomeCategoryRow
import com.rhesdev.warta.feature.news.presentation.home.components.TrendStrip
import com.rhesdev.warta.feature.news.presentation.home.components.homeCategories

// Home screen wiring state to content plus previews.
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNewsClick = onNewsClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { onEvent(HomeUiEvent.OnRefresh) },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.defaultMargin)
        ) {
        item {
            HomeCategoryRow(
                categories = homeCategories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { onEvent(HomeUiEvent.OnCategorySelected(it)) }
            )
        }

        if (uiState.trendByDay.isNotEmpty()) {
            item {
                TrendStrip(
                    byDay = uiState.trendByDay,
                    selectedDay = uiState.selectedDay,
                    onDaySelected = { onEvent(HomeUiEvent.OnDaySelected(it)) }
                )
            }
        }

        when {
            uiState.isLoading -> StatePlaceholder {
                LoadingScreen()
            }
            uiState.error != null && uiState.filteredNews.isEmpty() -> StatePlaceholder {
                ErrorState(
                    title = stringResource(R.string.error_news_load),
                    message = uiState.error ?: stringResource(R.string.error_generic),
                    onRetry = { onEvent(HomeUiEvent.OnRetry) }
                )
            }
            uiState.filteredNews.isEmpty() -> StatePlaceholder {
                EmptyState(
                    title = stringResource(R.string.empty_news),
                    message = stringResource(R.string.empty_news_category)
                )
            }
            else -> NewsSections(
                uiState = uiState,
                onEvent = onEvent,
                onNewsClick = onNewsClick
            )
        }
        }
    }
}

private fun LazyListScope.StatePlaceholder(content: @Composable () -> Unit) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillParentMaxHeight(0.7f),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

private fun LazyListScope.NewsSections(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNewsClick: (String) -> Unit
) {
    item {
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.smallPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.custom12dpMargin)
        ) {
            items(uiState.headlineNews, key = { it.link }) { news ->
                HeadlineCard(
                    news = news,
                    onClick = { onNewsClick(news.link) }
                )
            }
        }
    }

    uiState.popularNews?.let { popular ->
        item {
            SectionHeader(
                stringResource(R.string.section_popular_now),
                modifier = Modifier.padding(horizontal = Dimens.smallPadding)
            )
        }
        item {
            PopularNewsCard(
                news = popular,
                onClick = { onNewsClick(popular.link) },
                onShareClick = { },
                modifier = Modifier.padding(horizontal = Dimens.smallPadding)
            )
        }
    }

    if (uiState.trendingNews.isNotEmpty()) {
        item {
            SectionHeader(
                stringResource(R.string.section_trending_now),
                modifier = Modifier.padding(horizontal = Dimens.smallPadding)
            )
        }
        items(uiState.trendingNews, key = { it.link }) { news ->
            TrendingNewsItem(
                news = news,
                onClick = { onNewsClick(news.link) },
                modifier = Modifier.padding(horizontal = Dimens.smallPadding)
            )
        }
    }

    if (uiState.selectedCategory == null && uiState.selectedDay == null &&
        uiState.filteredNews.isNotEmpty()
    ) {
        item {
            if (uiState.isLoadingMore) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.smallPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LaunchedEffect(uiState.filteredNews.size) {
                    onEvent(HomeUiEvent.OnLoadMore)
                }
            }
        }
    }
}

private val sampleNews = listOf(
    News(
        link = "https://example.com/1",
        title = "Ekonomi Indonesia Tumbuh Pesat di Q1 2024",
        contentSnippet = "Pertumbuhan ekonomi Indonesia mencapai 5.03% pada kuartal pertama 2024.",
        isoDate = "2 jam lalu",
        imageUrl = "",
        source = "CNN",
        category = "society"
    ),
    News(
        link = "https://example.com/2",
        title = "Timnas Indonesia Lolos ke Piala Asia 2025",
        contentSnippet = "Timnas Indonesia berhasil lolos ke Piala Asia 2025.",
        isoDate = "3 jam lalu",
        imageUrl = "",
        source = "Tribun",
        category = "sports"
    ),
    News(
        link = "https://example.com/3",
        title = "Teknologi AI Semakin Berkembang di Indonesia",
        contentSnippet = "Penggunaan teknologi AI meningkat pesat di berbagai sektor.",
        isoDate = "4 jam lalu",
        imageUrl = "",
        source = "Detik",
        category = "technology"
    ),
    News(
        link = "https://example.com/4",
        title = "Festival Budaya Nusantara 2024",
        contentSnippet = "Festival budaya tahunan kembali digelar dengan meriah.",
        isoDate = "5 jam lalu",
        imageUrl = "",
        source = "Kompas",
        category = "entertainment"
    ),
    News(
        link = "https://example.com/5",
        title = "Tips Sehat di Musim Hujan",
        contentSnippet = "Pakar kesehatan berbagi tips menjaga kesehatan di musim hujan.",
        isoDate = "6 jam lalu",
        imageUrl = "",
        source = "Health",
        category = "health"
    )
)

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(
                allNews = sampleNews,
                selectedCategory = null,
                isLoading = false
            ),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentLoadingPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(isLoading = true),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentErrorPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(error = "Gagal memuat berita"),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentEmptyPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(isLoading = false),
            onEvent = {},
            onNewsClick = {}
        )
    }
}
