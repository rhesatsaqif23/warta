package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/** Warta brand logo loaded from assets/img_logo.png. */
@Composable
fun WartaLogo(modifier: Modifier = Modifier) {
    AsyncImage(
        model = "file:///android_asset/img_logo.png",
        contentDescription = "Warta",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(40.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun WartaLogoPreview() {
    WartaLogo()
}
