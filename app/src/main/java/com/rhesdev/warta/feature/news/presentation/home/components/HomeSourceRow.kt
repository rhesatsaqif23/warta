package com.rhesdev.warta.feature.news.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhesdev.warta.core.presentation.components.CategoryChip
import com.rhesdev.warta.core.presentation.theme.WartaTheme

// Publisher filter row backed by the API hosts facet.
@Composable
fun HomeSourceRow(
    sources: List<String>,
    selectedSource: String?,
    onSourceSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sources, key = { it }) { host ->
            CategoryChip(
                label = host.removePrefix("www."),
                selected = selectedSource == host,
                onClick = { onSourceSelected(if (host == selectedSource) null else host) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSourceRowPreview() {
    WartaTheme {
        HomeSourceRow(
            sources = listOf("koran-jakarta.com", "news.republika.co.id", "voi.id"),
            selectedSource = null,
            onSourceSelected = {}
        )
    }
}
