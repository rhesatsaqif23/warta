package com.rhesdev.warta.feature.news.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.rhesdev.warta.core.presentation.components.ShimmerBox
import com.rhesdev.warta.core.presentation.theme.WartaTheme

// News image with shimmer loading and placeholder fallback.
@Composable
fun NewsImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (imageUrl.isBlank()) {
        PlaceholderBox(modifier = modifier)
        return
    }
    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize(),
            loading = { ShimmerBox(modifier = Modifier.matchParentSize()) },
            error = { PlaceholderBox(modifier = Modifier.matchParentSize()) }
        )
    }
}

@Composable
private fun PlaceholderBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsImageFallbackPreview() {
    WartaTheme {
        NewsImage(
            imageUrl = "",
            contentDescription = null,
            modifier = Modifier.size(width = 138.dp, height = 84.dp)
        )
    }
}
