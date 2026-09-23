package com.rhesdev.warta.feature.search.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.ErrorState
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.components.NewsCard
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.domain.model.News

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNewsClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = uiState.query,
                        onValueChange = { viewModel.onEvent(SearchUiEvent.OnQueryChanged(it)) },
                        placeholder = { Text("Cari berita...") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        trailingIcon = {
                            if (uiState.query.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onEvent(SearchUiEvent.OnClearQuery) }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Hapus")
                                }
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        SearchContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onNewsClick = onNewsClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun SearchContent(
    uiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit,
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.error != null && uiState.results.isEmpty() -> {
                ErrorState(
                    title = "Pencarian gagal",
                    message = uiState.error ?: "Terjadi kesalahan",
                    onRetry = { onEvent(SearchUiEvent.OnQueryChanged(uiState.query)) }
                )
            }
            uiState.results.isEmpty() && uiState.query.isNotBlank() -> {
                EmptyState(
                    title = "Tidak ada hasil",
                    message = "Tidak ada hasil untuk \"${uiState.query}\"",
                    icon = Icons.Outlined.Search
                )
            }
            else -> {
                LazyColumn {
                    items(uiState.results, key = { it.link }) { news ->
                        NewsCard(
                            news = news,
                            onClick = { onNewsClick(news.link) }
                        )
                    }
                }
            }
        }
    }
}

private val sampleResults = listOf(
    News(
        link = "https://example.com/1",
        title = "Ekonomi Indonesia Tumbuh Pesat",
        contentSnippet = "Pertumbuhan ekonomi Indonesia mencapai 5.03%.",
        isoDate = "2 jam lalu",
        imageUrl = "",
        source = "CNN",
        category = "society"
    ),
    News(
        link = "https://example.com/2",
        title = "Tips Ekonomi di Masa Sulit",
        contentSnippet = "Pakar ekonomi berbagi tips menghadapi inflasi.",
        isoDate = "3 jam lalu",
        imageUrl = "",
        source = "Kompas",
        category = "economy"
    )
)

@Preview(showBackground = true)
@Composable
private fun SearchContentPreview() {
    WartaTheme {
        SearchContent(
            uiState = SearchUiState(
                query = "ekonomi",
                results = sampleResults,
                isLoading = false
            ),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchContentLoadingPreview() {
    WartaTheme {
        SearchContent(
            uiState = SearchUiState(
                query = "ekonomi",
                isLoading = true
            ),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchContentEmptyPreview() {
    WartaTheme {
        SearchContent(
            uiState = SearchUiState(
                query = "xyz123",
                results = emptyList(),
                isLoading = false
            ),
            onEvent = {},
            onNewsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchContentErrorPreview() {
    WartaTheme {
        SearchContent(
            uiState = SearchUiState(
                query = "ekonomi",
                error = "Gagal mencari berita"
            ),
            onEvent = {},
            onNewsClick = {}
        )
    }
}
