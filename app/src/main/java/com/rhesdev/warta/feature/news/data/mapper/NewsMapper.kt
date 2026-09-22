package com.rhesdev.warta.feature.news.data.mapper

import com.rhesdev.warta.feature.news.data.local.NewsEntity
import com.rhesdev.warta.feature.news.data.remote.dto.NewsDto
import com.rhesdev.warta.feature.news.domain.model.News

/** Convert API DTO to Room Entity. Handles nullable fields. */
fun NewsDto.toEntity(source: String, category: String): NewsEntity {
    return NewsEntity(
        link = link ?: "",
        title = title ?: "",
        contentSnippet = contentSnippet ?: "",
        isoDate = isoDate ?: "",
        imageUrl = image?.small ?: image?.large ?: "",
        source = source,
        category = category
    )
}

/** Convert Room Entity to Domain Model. */
fun NewsEntity.toDomain(): News {
    return News(
        link = link,
        title = title,
        contentSnippet = contentSnippet,
        isoDate = isoDate,
        imageUrl = imageUrl,
        source = source,
        category = category
    )
}
