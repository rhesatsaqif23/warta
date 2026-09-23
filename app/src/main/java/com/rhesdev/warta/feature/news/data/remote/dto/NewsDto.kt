package com.rhesdev.warta.feature.news.data.remote.dto

import com.google.gson.annotations.SerializedName

// API article DTO with nullable fields.
data class NewsDto(
    @SerializedName("id") val id: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("host") val host: String?,
    @SerializedName("sitename") val sitename: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("categories") val categories: List<String>?
)
