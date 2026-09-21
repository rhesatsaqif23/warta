package com.rhesdev.warta.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

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
