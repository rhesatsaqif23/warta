package com.rhesdev.warta.feature.news.domain.model

// Aggregate counts for one news slice (exact total + facets).
data class NewsStats(
    val total: Int = 0,
    val hosts: Map<String, Int> = emptyMap(),
    val byDay: Map<String, Int> = emptyMap()
)
