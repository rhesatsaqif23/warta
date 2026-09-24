package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Fetch one publisher's news and refresh the local cache.
class RefreshNewsByHostUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(host: String) =
        repository.refreshNewsByHost(host)
}
