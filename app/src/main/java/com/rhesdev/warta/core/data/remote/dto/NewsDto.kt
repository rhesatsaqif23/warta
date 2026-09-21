package com.rhesdev.warta.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("title") val title: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("contentSnippet") val contentSnippet: String?,
    @SerializedName("isoDate") val isoDate: String?,
    @SerializedName("image") val image: ImageDto?
)

data class ImageDto(
    @SerializedName("small") val small: String?,
    @SerializedName("large") val large: String?
)
