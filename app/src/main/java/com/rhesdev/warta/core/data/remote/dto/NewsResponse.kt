package com.rhesdev.warta.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("data") val data: List<NewsDto>
)
