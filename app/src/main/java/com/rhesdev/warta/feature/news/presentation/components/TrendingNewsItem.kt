package com.rhesdev.warta.feature.news.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.rhesdev.warta.core.utils.DateFormatter
import com.rhesdev.warta.core.utils.Dimens
import com.rhesdev.warta.feature.news.domain.model.News

// Horizontal trending news row.
@Composable
fun TrendingNewsItem(
    news: News,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(Dimens.defaultElevation, RoundedCornerShape(Dimens.bigRadius))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.bigRadius))
            .clickable(onClick = onClick)
            .padding(Dimens.smallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = DateFormatter.formatCardDate(news.isoDate),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = Dimens.xxsPadding)
            )
        }
        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(Dimens.newsThumbWidth)
                .height(Dimens.newsThumbHeight)
                .clip(RoundedCornerShape(Dimens.custom10dpRadius))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrendingNewsItemPreview() {
    TrendingNewsItem(
        news = News(
            link = "https://example.com",
            title = "Trending: Teknologi AI Terbaru",
            contentSnippet = "Description",
            isoDate = "2024-12-11T11:21:00.000Z",
            imageUrl = "",
            source = "Detik",
            category = "teknologi"
        ),
        onClick = {}
    )
}
