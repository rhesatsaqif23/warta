package com.rhesdev.warta.feature.home.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.core.presentation.components.HeadlineCard
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.components.PopularNewsCard
import com.rhesdev.warta.core.presentation.components.SectionHeader
import com.rhesdev.warta.core.presentation.components.TrendingNewsItem
import com.rhesdev.warta.core.presentation.theme.WartaTheme
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
        onNewsClick = onNewsClick,
        onSearchClick = onSearchClick,
        onCategoryClick = { viewModel.loadNews(it) }
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onNewsClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> LoadingScreen(modifier = modifier)
        uiState.error != null -> {
            ErrorContent(
                message = uiState.error ?: "Terjadi kesalahan",
                modifier = modifier
            )
        }
        uiState.topNews.isEmpty() -> {
            EmptyContent(modifier = modifier)
        }
        else -> {
            NewsListContent(
                uiState = uiState,
                onNewsClick = onNewsClick,
                onSearchClick = onSearchClick,
                onCategoryClick = onCategoryClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        modifier = modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Text(
        text = "Tidak ada berita",
        modifier = modifier.padding(16.dp)
    )
}

@Composable
private fun NewsListContent(
    uiState: HomeUiState,
    onNewsClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.headlineNews) { news ->
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
            items(uiState.trendingNews) { news ->
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
        category = "nasional"
    ),
    News(
        link = "https://example.com/2",
        title = "Timnas Indonesia Lolos ke Piala Asia 2025",
        contentSnippet = "Timnas Indonesia berhasil lolos ke Piala Asia 2025.",
        isoDate = "3 jam lalu",
        imageUrl = "",
        source = "Tribun",
        category = "olahraga"
    ),
    News(
        link = "https://example.com/3",
        title = "Teknologi AI Semakin Berkembang di Indonesia",
        contentSnippet = "Penggunaan teknologi AI meningkat pesat di berbagai sektor.",
        isoDate = "4 jam lalu",
        imageUrl = "",
        source = "Detik",
        category = "teknologi"
    ),
    News(
        link = "https://example.com/4",
        title = "Festival Budaya Nusantara 2024",
        contentSnippet = "Festival budaya tahunan kembali digelar dengan meriah.",
        isoDate = "5 jam lalu",
        imageUrl = "",
        source = "Kompas",
        category = "seni"
    ),
    News(
        link = "https://example.com/5",
        title = "Tips Sehat di Musim Hujan",
        contentSnippet = "Pakar kesehatan berbagi tips menjaga kesehatan di musim hujan.",
        isoDate = "6 jam lalu",
        imageUrl = "",
        source = "Health",
        category = "kesehatan"
    )
)

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(
                topNews = sampleNews,
                selectedCategory = "nasional",
                isLoading = false
            ),
            onNewsClick = {},
            onSearchClick = {},
            onCategoryClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentLoadingPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(isLoading = true),
            onNewsClick = {},
            onSearchClick = {},
            onCategoryClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentErrorPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(error = "Gagal memuat berita"),
            onNewsClick = {},
            onSearchClick = {},
            onCategoryClick = {}
        )
    }
}
