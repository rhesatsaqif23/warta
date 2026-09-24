package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Refresh cached news from the API.
class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke() = repository.refreshNews()
}
