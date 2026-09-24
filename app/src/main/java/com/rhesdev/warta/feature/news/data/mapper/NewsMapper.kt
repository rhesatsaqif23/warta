package com.rhesdev.warta.feature.news.data.mapper

import com.rhesdev.warta.feature.news.data.local.NewsEntity
import com.rhesdev.warta.feature.news.data.remote.dto.ArticleDto
import com.rhesdev.warta.feature.news.data.remote.dto.NewsDto
import com.rhesdev.warta.feature.news.data.remote.dto.StatsResponse
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.model.NewsStats

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

fun StatsResponse.toDomain(): NewsStats {
    return NewsStats(
        total = total,
        hosts = hosts ?: emptyMap(),
        byDay = byDay ?: emptyMap()
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
        category = category,
        content = content
    )
}

fun ArticleDto.toContent(): String = text ?: description ?: ""
