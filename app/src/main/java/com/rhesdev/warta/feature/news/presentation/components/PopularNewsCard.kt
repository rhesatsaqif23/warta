package com.rhesdev.warta.feature.news.presentation.components

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rhesdev.warta.R
import com.rhesdev.warta.core.utils.DateFormatter
import com.rhesdev.warta.core.utils.Dimens
import com.rhesdev.warta.feature.news.domain.model.News

// Popular news card with image, title, and actions.
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
            .shadow(Dimens.defaultElevation, RoundedCornerShape(Dimens.bigRadius))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.bigRadius))
            .clickable(onClick = onClick)
            .padding(bottom = Dimens.xsPadding)
    ) {
        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.popularImageHeight)
                .clip(RoundedCornerShape(Dimens.bigRadius))
        )
        Text(
            text = news.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            modifier = Modifier.padding(horizontal = Dimens.smallPadding, vertical = Dimens.xsPadding)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.smallPadding)
        ) {
            Text(
                text = DateFormatter.formatCardDate(news.isoDate),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onShareClick) {
                Icon(Icons.Default.Share, contentDescription = stringResource(R.string.cd_share))
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_more))
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
            isoDate = "2024-12-11T11:21:00.000Z",
            imageUrl = "",
            source = "Tribun",
            category = "nasional"
        ),
        onClick = {},
        onShareClick = {}
    )
}
