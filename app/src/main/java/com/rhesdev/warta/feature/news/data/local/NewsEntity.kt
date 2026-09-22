package com.rhesdev.warta.feature.news.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Room entity for news articles stored locally. */
@Entity(
    tableName = "news_table",
    indices = [
        Index(value = ["category"]),
        Index(value = ["source"]),
        Index(value = ["isoDate"])
    ]
)
data class NewsEntity(
    @PrimaryKey val link: String,
    val title: String,
    val contentSnippet: String,
    val isoDate: String,
    val imageUrl: String,
    val source: String,
    val category: String
)
