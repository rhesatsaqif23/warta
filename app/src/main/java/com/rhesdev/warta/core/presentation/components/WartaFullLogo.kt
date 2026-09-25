package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.rhesdev.warta.R
import com.rhesdev.warta.core.utils.Dimens

// Warta logo with text from assets.
@Composable
fun WartaFullLogo(
    modifier: Modifier = Modifier,
    width: Dp = Dimens.logoWidth
) {
    AsyncImage(
        model = "file:///android_asset/img_logo_with_text.png",
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Fit,
        modifier = modifier.width(width)
    )
}

@Preview(showBackground = true)
@Composable
private fun WartaFullLogoPreview() {
    WartaFullLogo()
}
