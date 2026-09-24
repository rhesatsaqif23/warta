package com.rhesdev.warta.feature.news.domain.model

// News domain model.
data class News(
    val link: String,
    val title: String,
    val contentSnippet: String,
    val isoDate: String,
    val imageUrl: String,
    val source: String,
    val category: String,
    val content: String = ""
)
