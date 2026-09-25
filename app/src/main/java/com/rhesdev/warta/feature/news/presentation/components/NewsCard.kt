package com.rhesdev.warta.feature.news.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.rhesdev.warta.core.utils.DateFormatter
import com.rhesdev.warta.core.utils.Dimens
import com.rhesdev.warta.feature.news.domain.model.News

// Generic news row with thumbnail, title, and source.
@Composable
fun NewsCard(
    news: News,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(Dimens.smallPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(Dimens.cardThumbWidth)
                .height(Dimens.cardThumbHeight)
                .clip(RoundedCornerShape(Dimens.defaultRadius))
        )
        Spacer(modifier = Modifier.width(Dimens.custom12dpMargin))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(Dimens.xsMargin))
            Text(
                text = news.source.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = DateFormatter.formatCardDate(news.isoDate),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsCardPreview() {
    NewsCard(
        news = News(
            link = "https://example.com",
            title = "Breaking News: Indonesia Economic Growth Exceeds Expectations",
            contentSnippet = "Short description",
            isoDate = "2024-12-11T11:21:00.000Z",
            imageUrl = "",
            source = "CNN",
            category = "nasional"
        ),
        onClick = {}
    )
}
