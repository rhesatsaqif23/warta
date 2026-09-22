package com.rhesdev.warta.core.domain.model

/** Domain model for news article. Pure Kotlin, no Android dependencies. */
data class News(
    val link: String,
    val title: String,
    val contentSnippet: String,
    val isoDate: String,
    val imageUrl: String,
    val source: String,
    val category: String
)
