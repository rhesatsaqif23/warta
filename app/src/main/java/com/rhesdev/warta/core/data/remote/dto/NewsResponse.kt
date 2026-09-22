package com.rhesdev.warta.core.data.remote.dto

import com.google.gson.annotations.SerializedName

/** API response wrapper containing list of news items. */
data class NewsResponse(
    @SerializedName("data") val data: List<NewsDto>
)
