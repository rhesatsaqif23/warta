package com.rhesdev.warta.core.data.remote.dto

import com.google.gson.annotations.SerializedName

/** DTO for news article from API. Fields are nullable. */
data class NewsDto(
    @SerializedName("title") val title: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("contentSnippet") val contentSnippet: String?,
    @SerializedName("isoDate") val isoDate: String?,
    @SerializedName("image") val image: ImageDto?
)

/** Nested DTO for news image URLs. */
data class ImageDto(
    @SerializedName("small") val small: String?,
    @SerializedName("large") val large: String?
)
