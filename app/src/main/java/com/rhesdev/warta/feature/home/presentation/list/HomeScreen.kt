package com.rhesdev.warta.feature.home.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhesdev.warta.core.presentation.components.CategoryChip
import com.rhesdev.warta.core.presentation.components.HeadlineCard
import com.rhesdev.warta.core.presentation.components.LoadingScreen
import com.rhesdev.warta.core.presentation.components.PopularNewsCard
import com.rhesdev.warta.core.presentation.components.SearchField
import com.rhesdev.warta.core.presentation.components.SectionHeader
import com.rhesdev.warta.core.presentation.components.TrendingNewsItem
import com.rhesdev.warta.core.presentation.components.WartaBottomNavigationBar
import com.rhesdev.warta.core.presentation.components.WartaLogo

private val categories = listOf(
    "nasional", "kesehatan", "bisnis", "seni", "olahraga",
    "teknologi", "hiburan", "gaya-hidup"
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNewsClick: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WartaLogo()
                    SearchField(onClick = onSearchClick, modifier = Modifier.weight(1f))
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            label = category.replaceFirstChar { it.uppercase() },
                            selected = uiState.selectedCategory == category,
                            onClick = { viewModel.loadNews(category) }
                        )
                    }
                }
            }
        },
        bottomBar = {
            WartaBottomNavigationBar(
                currentRoute = "home",
                onItemClick = { }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Terjadi kesalahan",
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.topNews.isEmpty() -> {
                Text(
                    text = "Tidak ada berita",
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
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
        }
    }
}
