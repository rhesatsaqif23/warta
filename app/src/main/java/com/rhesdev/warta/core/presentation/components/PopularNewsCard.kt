package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rhesdev.warta.core.domain.model.News

/** Popular news card with image, title, date, and share/more actions. */
@Composable
fun PopularNewsCard(
    news: News,
    onClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .padding(bottom = 8.dp)
    ) {
        AsyncImage(
            model = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(178.dp)
                .clip(RoundedCornerShape(15.dp))
        )
        Text(
            text = news.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = news.isoDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onShareClick) {
                Icon(Icons.Default.Share, contentDescription = "Bagikan")
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Lainnya")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PopularNewsCardPreview() {
    PopularNewsCard(
        news = News(
            link = "https://example.com",
            title = "Berita Populer Hari Ini",
            contentSnippet = "Description",
            isoDate = "3 jam lalu",
            imageUrl = "",
            source = "Tribun",
            category = "nasional"
        ),
        onClick = {},
        onShareClick = {}
    )
}
