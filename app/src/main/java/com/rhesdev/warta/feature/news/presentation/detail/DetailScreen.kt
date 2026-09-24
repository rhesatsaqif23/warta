package com.rhesdev.warta.feature.news.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.core.presentation.components.ErrorState
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.feature.news.presentation.components.NewsImage
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.domain.model.News

// Detail screen showing one article with share and open-in-browser actions.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = { Text("Detail Berita") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    uiState.news?.let { news ->
                        IconButton(onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "${news.title}\n\n${news.link}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Berita"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Bagikan")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        DetailContent(
            uiState = uiState,
            onReadMore = { link ->
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            },
            onRetry = { viewModel.retry() },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DetailContent(
    uiState: DetailUiState,
    onReadMore: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> LoadingScreen(modifier = modifier)
        uiState.error != null && uiState.news == null -> {
            ErrorState(
                title = "Gagal memuat berita",
                message = uiState.error ?: "Terjadi kesalahan",
                onRetry = onRetry,
                modifier = modifier
            )
        }
        uiState.news != null -> {
            NewsDetailContent(
                news = uiState.news,
                onReadMore = onReadMore,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun NewsDetailContent(
    news: News,
    onReadMore: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = news.source.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = news.title,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = news.isoDate,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = news.contentSnippet,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = { onReadMore(news.link) }) {
                Text("Baca Selengkapnya")
            }
        }
    }
}

private val sampleNews = News(
    link = "https://example.com",
    title = "Ekonomi Indonesia Tumbuh Pesat di Q1 2024",
    contentSnippet = "Pertumbuhan ekonomi Indonesia mencapai 5.03% pada kuartal pertama 2024. Hal ini menunjukkan pemulihan ekonomi yang solid pasca pandemi. Para ahli ekonomi memprediksi pertumbuhan ini akan berlanjut sepanjang tahun 2024.",
    isoDate = "2 jam lalu",
    imageUrl = "",
    source = "CNN",
    category = "society"
)

@Preview(showBackground = true)
@Composable
private fun DetailContentPreview() {
    WartaTheme {
        DetailContent(
            uiState = DetailUiState(
                news = sampleNews,
                isLoading = false
            ),
            onReadMore = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentLoadingPreview() {
    WartaTheme {
        DetailContent(
            uiState = DetailUiState(isLoading = true),
            onReadMore = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentErrorPreview() {
    WartaTheme {
        DetailContent(
            uiState = DetailUiState(error = "Gagal memuat berita"),
            onReadMore = {},
            onRetry = {}
        )
    }
}
