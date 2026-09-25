package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rhesdev.warta.R

// Warta brand logo from assets.
@Composable
fun WartaLogo(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    AsyncImage(
        model = "file:///android_asset/img_logo.png",
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size)
    )
}

@Preview(showBackground = true)
@Composable
private fun WartaLogoPreview() {
    WartaLogo()
}
