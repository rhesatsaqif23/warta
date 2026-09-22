package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

/** Use case to refresh news from API and store in database. */
class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke() = repository.refreshNews()
}
