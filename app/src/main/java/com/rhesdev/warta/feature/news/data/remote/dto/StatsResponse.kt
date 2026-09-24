package com.rhesdev.warta.feature.news.data.remote.dto

import com.google.gson.annotations.SerializedName

// Aggregate counts for one API slice (facets, never capped).
data class StatsResponse(
    @SerializedName("total") val total: Int = 0,
    @SerializedName("hosts") val hosts: Map<String, Int>? = null,
    @SerializedName("by_day") val byDay: Map<String, Int>? = null
)
