package com.rhesdev.warta.feature.home.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.ErrorState
import com.rhesdev.warta.core.presentation.components.HeadlineCard
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.components.PopularNewsCard
import com.rhesdev.warta.core.presentation.components.SectionHeader
import com.rhesdev.warta.core.presentation.components.TrendingNewsItem
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.home.presentation.list.components.HomeCategoryRow
import com.rhesdev.warta.feature.home.presentation.list.components.homeCategories
import com.rhesdev.warta.feature.news.domain.model.News

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNewsClick: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNewsClick = onNewsClick,
        onSearchClick = onSearchClick
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNewsClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> LoadingScreen(modifier = modifier)
        uiState.error != null && uiState.filteredNews.isEmpty() -> {
            ErrorState(
                title = "Gagal memuat berita",
                message = uiState.error ?: "Terjadi kesalahan",
                onRetry = { onEvent(HomeUiEvent.OnRetry) },
                modifier = modifier
            )
        }
        uiState.filteredNews.isEmpty() -> {
            EmptyState(
                title = "Tidak ada berita",
                message = "Belum ada berita untuk kategori ini",
                modifier = modifier
            )
        }
        else -> {
            NewsListContent(
                uiState = uiState,
                onEvent = onEvent,
                onNewsClick = onNewsClick,
                onSearchClick = onSearchClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun NewsListContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNewsClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeCategoryRow(
                categories = homeCategories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { onEvent(HomeUiEvent.OnCategorySelected(it)) }
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.headlineNews, key = { it.link }) { news ->
                    HeadlineCard(
                        news = news,
                        onReadClick = { onNewsClick(news.link) }
                    )
                }
            }
        }

        uiState.popularNews?.let { popular ->
            item {
                SectionHeader(
                    "Popular Now",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                PopularNewsCard(
                    news = popular,
                    onClick = { onNewsClick(popular.link) },
                    onShareClick = { },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (uiState.trendingNews.isNotEmpty()) {
            item {
                SectionHeader(
                    "Trending Now",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            items(uiState.trendingNews, key = { it.link }) { news ->
                TrendingNewsItem(
                    news = news,
                    onClick = { onNewsClick(news.link) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
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
            onNewsClick = {},
            onSearchClick = {}
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
            onNewsClick = {},
            onSearchClick = {}
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
            onNewsClick = {},
            onSearchClick = {}
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
            onNewsClick = {},
            onSearchClick = {}
        )
    }
}
