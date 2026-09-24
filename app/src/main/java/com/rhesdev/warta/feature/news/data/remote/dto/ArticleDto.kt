package com.rhesdev.warta.feature.news.data.remote.dto

import com.google.gson.annotations.SerializedName

// Single article DTO with full body text.
data class ArticleDto(
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("host") val host: String?,
    @SerializedName("sitename") val sitename: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("categories") val categories: List<String>?,
    @SerializedName("text") val text: String?
)
