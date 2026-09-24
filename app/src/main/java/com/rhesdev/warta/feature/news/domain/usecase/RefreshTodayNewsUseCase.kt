package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Fetch today's news and refresh the local cache.
class RefreshTodayNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke() =
        repository.refreshTodayNews()
}
