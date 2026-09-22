package com.rhesdev.warta.feature.home.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.core.presentation.components.CategoryChip
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
        onCategoryClick = { viewModel.loadNews(if (it == uiState.selectedCategory) null else it) },
        onRetry = { viewModel.loadNews() }
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onNewsClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    when {
        uiState.isLoading -> LoadingScreen(modifier = modifier)
        uiState.error != null && uiState.filteredNews.isEmpty() -> {
            ErrorContent(
                message = uiState.error ?: "Terjadi kesalahan",
                onRetry = onRetry,
                modifier = modifier
            )
        }
        uiState.filteredNews.isEmpty() -> {
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
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Gagal memuat berita",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Coba Lagi")
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Inbox,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tidak ada berita",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Belum ada berita untuk kategori ini",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

private val categories = listOf(
    "all" to "Semua",
    "society" to "Nasional",
    "technology" to "Teknologi",
    "economy" to "Ekonomi",
    "sports" to "Olahraga",
    "entertainment" to "Hiburan",
    "politics" to "Politik",
    "health" to "Kesehatan"
)

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
                items(categories) { (key, label) ->
                    CategoryChip(
                        label = label,
                        selected = if (key == "all") uiState.selectedCategory == null else uiState.selectedCategory == key,
                        onClick = {
                            val category = if (key == "all") null else key
                            onCategoryClick(category ?: key)
                        }
                    )
                }
            }
        }

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
                allNews = sampleNews,
                selectedCategory = null,
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
            onCategoryClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentEmptyPreview() {
    WartaTheme {
        HomeContent(
            uiState = HomeUiState(isLoading = false),
            onNewsClick = {},
            onSearchClick = {},
            onCategoryClick = {}
        )
    }
}
