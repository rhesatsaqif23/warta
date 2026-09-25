package com.rhesdev.warta.feature.news.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.R
import com.rhesdev.warta.core.presentation.components.EditableSearchField
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.ErrorState
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.theme.Primary
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.core.utils.Dimens
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.presentation.components.TrendingNewsItem
import com.rhesdev.warta.feature.news.presentation.home.components.HomeCategoryRow
import com.rhesdev.warta.feature.news.presentation.home.components.homeCategories

// Search screen with autofocus field and debounced results.
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNewsClick: (String) -> Unit,
    initialQuery: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(initialQuery) {
        if (!initialQuery.isNullOrBlank()) {
            viewModel.onEvent(SearchUiEvent.OnQueryChanged(initialQuery))
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.smallPadding, vertical = Dimens.xsPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                EditableSearchField(
                    value = uiState.query,
                    onValueChange = { viewModel.onEvent(SearchUiEvent.OnQueryChanged(it)) },
                    onClearClick = { viewModel.onEvent(SearchUiEvent.OnClearQuery) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimens.customPaddingLabel)
                        .focusRequester(focusRequester),
                    focusRequester = focusRequester
                )
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.cd_menu),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
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

private fun categoryKeyFor(query: String): String? {
    if (query.isBlank()) return null
    return homeCategories.firstOrNull { it.query?.equals(query, ignoreCase = true) == true }?.key
        ?: "__none__"
}

@Composable
fun SearchContent(
    uiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit,
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeCategoryRow(
            categories = homeCategories,
            selectedCategory = categoryKeyFor(uiState.query),
            onCategorySelected = { key ->
                val category = homeCategories.firstOrNull { it.key == key }
                onEvent(SearchUiEvent.OnQueryChanged(category?.query ?: ""))
            },
            modifier = Modifier.padding(vertical = Dimens.xsPadding)
        )
        if (uiState.query.isNotBlank()) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.search_for_prefix))
                    withStyle(SpanStyle(color = Primary)) { append(uiState.query) }
                    append(stringResource(R.string.search_for_suffix))
                },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = Dimens.smallPadding, vertical = Dimens.xsPadding)
            )
        }
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.error != null && uiState.results.isEmpty() -> {
                ErrorState(
                    title = stringResource(R.string.error_search),
                    message = uiState.error ?: stringResource(R.string.error_generic),
                    onRetry = { onEvent(SearchUiEvent.OnQueryChanged(uiState.query)) }
                )
            }
            uiState.results.isEmpty() && uiState.query.isNotBlank() -> {
                EmptyState(
                    title = stringResource(R.string.empty_search),
                    message = stringResource(R.string.empty_search_result, uiState.query),
                    illustrationModel = "file:///android_asset/img_search_empty.png"
                )
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = Dimens.smallPadding, vertical = Dimens.xsPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimens.defaultMargin)
                ) {
                    items(uiState.results, key = { it.link }) { news ->
                        TrendingNewsItem(
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
        isoDate = "2024-12-11T11:21:00.000Z",
        imageUrl = "",
        source = "CNN",
        category = "society"
    ),
    News(
        link = "https://example.com/2",
        title = "Tips Ekonomi di Masa Sulit",
        contentSnippet = "Pakar ekonomi berbagi tips menghadapi inflasi.",
        isoDate = "2024-12-11T11:21:00.000Z",
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
