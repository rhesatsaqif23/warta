package com.rhesdev.warta.feature.news.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.core.presentation.components.ErrorState
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.theme.Accent
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.core.utils.DateFormatter
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.presentation.components.NewsImage
import com.rhesdev.warta.feature.news.presentation.home.components.HomeTopBar

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun shareArticle(news: News) {
        val excerpt = uiState.fullText?.take(500) ?: news.contentSnippet
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${news.title}\n\n$excerpt\n\n${news.link}")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Berita"))
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                onSearchClick = onSearchClick,
                // Menu icon doubles as the share action; no dedicated share icon in this layout.
                onMenuClick = { uiState.news?.let(::shareArticle) }
            )
        }
    ) { paddingValues ->
        DetailContent(
            uiState = uiState,
            onLoadFullText = { viewModel.loadFullText() },
            onOpenInBrowser = { link ->
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            },
            onRetry = { viewModel.retry() },
            onBackClick = onBackClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun DetailContent(
    uiState: DetailUiState,
    onLoadFullText: () -> Unit,
    onOpenInBrowser: (String) -> Unit,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
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
                fullText = uiState.fullText,
                isLoadingBody = uiState.isLoadingBody,
                bodyError = uiState.bodyError,
                onLoadFullText = onLoadFullText,
                onOpenInBrowser = onOpenInBrowser,
                onBackClick = onBackClick,
                modifier = modifier
            )
        }
    }
}

// Highlights the "<Kota>, CNN Indonesia -- " dateline article bodies conventionally open with.
private val datelinePattern = Regex("^([^,\\n]{1,60}, CNN Indonesia)\\s*--?\\s*")

private fun highlightDateline(text: String, accent: Color): AnnotatedString {
    val match = datelinePattern.find(text)
    return buildAnnotatedString {
        if (match != null) {
            withStyle(SpanStyle(color = accent)) { append(match.value) }
            append(text.substring(match.value.length))
        } else {
            append(text)
        }
    }
}

@Composable
private fun NewsDetailContent(
    news: News,
    fullText: String?,
    isLoadingBody: Boolean,
    bodyError: String?,
    onLoadFullText: () -> Unit,
    onOpenInBrowser: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            NewsImage(
                imageUrl = news.imageUrl,
                contentDescription = news.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White
                )
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = news.category.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = Accent
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = news.source,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = DateFormatter.formatDetailDate(news.isoDate),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            val bodyText = fullText ?: news.contentSnippet
            Text(
                text = highlightDateline(bodyText, Accent),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            when {
                isLoadingBody -> LoadingScreen()
                fullText != null -> {
                    TextButton(onClick = { onOpenInBrowser(news.link) }) {
                        Text("Buka di Browser")
                    }
                }
                bodyError != null -> {
                    Text(
                        text = bodyError,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    TextButton(onClick = { onOpenInBrowser(news.link) }) {
                        Text("Buka di Browser")
                    }
                }
                else -> {
                    TextButton(onClick = onLoadFullText) {
                        Text("Baca Selengkapnya")
                    }
                }
            }
        }
    }
}

private val sampleNews = News(
    link = "https://example.com",
    title = "STY Sebut Pemain Lelah, Pastikan Rotasi di Indonesia vs Laos",
    contentSnippet = "Solo, CNN Indonesia -- Pelatih Timnas Indonesia, Shin Tae Yong, menyebut stamina para pemain sedang terpengaruh jelang lawan Laos pada lanjutan Piala AFF 2024 di Stadion Manahan, Solo, Kamis (12/12). Kondisi itu membuat STY akan melakukan rotasi pemain.",
    isoDate = "2024-12-11T11:21:00.000Z",
    imageUrl = "",
    source = "CNN Indonesia",
    category = "olahraga"
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
            onLoadFullText = {},
            onOpenInBrowser = {},
            onRetry = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentLoadingPreview() {
    WartaTheme {
        DetailContent(
            uiState = DetailUiState(isLoading = true),
            onLoadFullText = {},
            onOpenInBrowser = {},
            onRetry = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentErrorPreview() {
    WartaTheme {
        DetailContent(
            uiState = DetailUiState(error = "Gagal memuat berita"),
            onLoadFullText = {},
            onOpenInBrowser = {},
            onRetry = {},
            onBackClick = {}
        )
    }
}
