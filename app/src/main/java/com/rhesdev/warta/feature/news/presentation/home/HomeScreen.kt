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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
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
import kotlinx.coroutines.flow.flowOf

private const val HEADER_COUNT = 4

// Home screen wiring paged state to content plus previews.
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagingItems = viewModel.homeFeed.collectAsLazyPagingItems()

    HomeContent(
        uiState = uiState,
        pagingItems = pagingItems,
        onEvent = viewModel::onEvent,
        onNewsClick = onNewsClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    pagingItems: LazyPagingItems<News>,
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
                uiState.error != null && pagingItems.itemCount == 0 -> StatePlaceholder {
                    ErrorState(
                        title = stringResource(R.string.error_news_load),
                        message = uiState.error ?: stringResource(R.string.error_generic),
                        onRetry = { onEvent(HomeUiEvent.OnRetry) }
                    )
                }
                pagingItems.itemCount == 0 -> StatePlaceholder {
                    EmptyState(
                        title = stringResource(R.string.empty_news),
                        message = stringResource(R.string.empty_news_category)
                    )
                }
                else -> NewsSections(
                    uiState = uiState,
                    pagingItems = pagingItems,
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

private data class HomeFeedSections(
    val headlines: List<News>,
    val popular: News?,
    val trending: List<News>
)

private fun buildFeedSections(
    pagingItems: LazyPagingItems<News>,
    uiState: HomeUiState
): HomeFeedSections {
    val headlines = mutableListOf<News>()
    var popular: News? = null
    val trending = mutableListOf<News>()
    var seen = 0
    for (index in 0 until pagingItems.itemCount) {
        val news = pagingItems[index] ?: continue
        if (news.matches(uiState)) {
            when (seen) {
                in 0..2 -> headlines.add(news)
                HEADER_COUNT - 1 -> popular = news
                else -> trending.add(news)
            }
            seen++
        }
    }
    return HomeFeedSections(headlines = headlines, popular = popular, trending = trending)
}

private fun News.matches(state: HomeUiState): Boolean {
    val categoryOk = state.selectedCategory == null || category == state.selectedCategory
    val dayOk = state.selectedDay == null || isoDate.startsWith(state.selectedDay)
    return categoryOk && dayOk
}

private fun LazyListScope.NewsSections(
    uiState: HomeUiState,
    pagingItems: LazyPagingItems<News>,
    onNewsClick: (String) -> Unit
) {
    val sections = buildFeedSections(pagingItems, uiState)

    if (sections.headlines.isNotEmpty()) {
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = Dimens.smallPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimens.custom12dpMargin)
            ) {
                items(sections.headlines, key = { it.link }) { news ->
                    HeadlineCard(
                        news = news,
                        onClick = { onNewsClick(news.link) }
                    )
                }
            }
        }
    }

    sections.popular?.let { popular ->
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

    item {
        SectionHeader(
            stringResource(R.string.section_trending_now),
            modifier = Modifier.padding(horizontal = Dimens.smallPadding)
        )
    }
    val itemKey = pagingItems.itemKey { news -> news.link }
    items(count = pagingItems.itemCount, key = itemKey) { index ->
        val news = pagingItems[index]
        if (index >= HEADER_COUNT && news != null && news.matches(uiState)) {
            TrendingNewsItem(
                news = news,
                onClick = { onNewsClick(news.link) },
                modifier = Modifier.padding(horizontal = Dimens.smallPadding)
            )
        }
    }

    when (pagingItems.loadState.append) {
        LoadState.Loading -> AppendFooter {
            CircularProgressIndicator()
        }
        is LoadState.Error -> AppendFooter {
            TextButton(onClick = { pagingItems.retry() }) {
                Text(stringResource(R.string.retry))
            }
        }
        else -> Unit
    }
}

private fun LazyListScope.AppendFooter(content: @Composable () -> Unit) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.smallPadding),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

private val sampleNews = listOf(
    News(
        link = "https://example.com/1",
        title = "Ekonomi Indonesia Tumbuh Pesat di Q1 2024",
        contentSnippet = "Pertumbuhan ekonomi Indonesia mencapai 5.03% pada kuartal pertama 2024.",
        isoDate = "2 jam lalu",
        imageUrl = "https://example.com/1.jpg",
        source = "CNN",
        category = "society"
    ),
    News(
        link = "https://example.com/2",
        title = "Timnas Indonesia Lolos ke Piala Asia 2025",
        contentSnippet = "Timnas Indonesia berhasil lolos ke Piala Asia 2025.",
        isoDate = "3 jam lalu",
        imageUrl = "https://example.com/2.jpg",
        source = "Tribun",
        category = "sports"
    ),
    News(
        link = "https://example.com/3",
        title = "Teknologi AI Semakin Berkembang di Indonesia",
        contentSnippet = "Penggunaan teknologi AI meningkat pesat di berbagai sektor.",
        isoDate = "4 jam lalu",
        imageUrl = "https://example.com/3.jpg",
        source = "Detik",
        category = "technology"
    ),
    News(
        link = "https://example.com/4",
        title = "Festival Budaya Nusantara 2024",
        contentSnippet = "Festival budaya tahunan kembali digelar dengan meriah.",
        isoDate = "5 jam lalu",
        imageUrl = "https://example.com/4.jpg",
        source = "Kompas",
        category = "entertainment"
    ),
    News(
        link = "https://example.com/5",
        title = "Tips Sehat di Musim Hujan",
        contentSnippet = "Pakar kesehatan berbagi tips menjaga kesehatan di musim hujan.",
        isoDate = "6 jam lalu",
        imageUrl = "https://example.com/5.jpg",
        source = "Health",
        category = "health"
    )
)

@Composable
private fun samplePagingItems(news: List<News>): LazyPagingItems<News> {
    val flow = remember(news) { flowOf(PagingData.from(news)) }
    return flow.collectAsLazyPagingItems()
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(isLoading = false),
            pagingItems = samplePagingItems(sampleNews),
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
            pagingItems = samplePagingItems(emptyList()),
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
            uiState = HomeUiState(error = "Gagal memuat berita", isLoading = false),
            pagingItems = samplePagingItems(emptyList()),
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
            pagingItems = samplePagingItems(emptyList()),
            onEvent = {},
            onNewsClick = {}
        )
    }
}