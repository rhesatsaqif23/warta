package com.rhesdev.warta.core.presentation.components

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
import androidx.compose.ui.unit.dp
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
            .shadow(4.dp, RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
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
                text = news.isoDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(138.dp)
                .height(84.dp)
                .clip(RoundedCornerShape(10.dp))
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
            isoDate = "5 jam lalu",
            imageUrl = "",
            source = "Detik",
            category = "teknologi"
        ),
        onClick = {}
    )
}
