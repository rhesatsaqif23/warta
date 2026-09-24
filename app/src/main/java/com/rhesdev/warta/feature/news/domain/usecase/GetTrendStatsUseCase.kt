package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.model.NewsStats
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Fetch aggregate counts and facets for the Indonesian slice.
class GetTrendStatsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): NewsStats =
        repository.getTrendStats()
}
