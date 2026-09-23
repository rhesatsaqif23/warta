package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// Warta brand logo from assets.
@Composable
fun WartaLogo(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    AsyncImage(
        model = "file:///android_asset/img_logo.png",
        contentDescription = "Warta",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size)
    )
}

@Preview(showBackground = true)
@Composable
private fun WartaLogoPreview() {
    WartaLogo()
}
