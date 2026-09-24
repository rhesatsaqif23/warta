package com.rhesdev.warta.feature.news.data.remote.dto

import com.google.gson.annotations.SerializedName

// API response wrapper holding the result list.
data class NewsResponse(
    @SerializedName("results") val results: List<NewsDto>
)
