package com.rhesdev.warta.feature.news.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.rhesdev.warta.core.utils.Dimens
import com.rhesdev.warta.feature.news.domain.model.News

// Large clickable headline card with gradient scrim.
@Composable
fun HeadlineCard(
    news: News,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(Dimens.headlineWidth)
            .height(Dimens.headlineHeight)
            .clip(RoundedCornerShape(Dimens.bigRadius))
            .clickable(onClick = onClick)
    ) {
        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        )
        Text(
            text = news.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 2,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.smallPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeadlineCardPreview() {
    HeadlineCard(
        news = News(
            link = "https://example.com",
            title = "Ekonomi Indonesia Tumbuh Pesat di Q1 2024",
            contentSnippet = "Description",
            isoDate = "2 jam lalu",
            imageUrl = "",
            source = "CNN",
            category = "ekonomi"
        ),
        onClick = {}
    )
}
