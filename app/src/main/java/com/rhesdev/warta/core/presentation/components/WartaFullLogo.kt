package com.rhesdev.warta.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage

/** Warta brand logo with text, loaded from assets/img_logo_with_text.png. */
@Composable
fun WartaFullLogo(modifier: Modifier = Modifier) {
    AsyncImage(
        model = "file:///android_asset/img_logo_with_text.png",
        contentDescription = "Warta",
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun WartaFullLogoPreview() {
    WartaFullLogo()
}
