package com.rhesdev.warta.feature.news.data.mapper

import com.rhesdev.warta.feature.news.data.local.NewsEntity
import com.rhesdev.warta.feature.news.data.remote.dto.NewsDto
import com.rhesdev.warta.feature.news.domain.model.News

// Maps API DTOs and Room entities to domain models.
fun NewsDto.toEntity(): NewsEntity {
    return NewsEntity(
        link = url ?: "",
        title = title ?: "",
        contentSnippet = description ?: "",
        isoDate = publishedAt ?: "",
        imageUrl = image ?: "",
        source = sitename ?: host ?: "",
        category = categories?.firstOrNull() ?: "general"
    )
}

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
