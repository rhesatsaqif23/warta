package com.rhesdev.warta.core.data.mapper

import com.rhesdev.warta.core.data.local.entity.NewsEntity
import com.rhesdev.warta.core.data.remote.dto.NewsDto
import com.rhesdev.warta.core.domain.model.News

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
