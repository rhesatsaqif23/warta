package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rhesdev.warta.core.presentation.theme.WartaTheme

// News image with placeholder fallback for blank or failed loads.
@Composable
fun NewsImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var loadFailed by remember(imageUrl) { mutableStateOf(imageUrl.isBlank()) }

    Box(modifier = modifier) {
        if (loadFailed) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = contentScale,
                onError = { loadFailed = true },
                modifier = Modifier.matchParentSize()
            )
        }
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
