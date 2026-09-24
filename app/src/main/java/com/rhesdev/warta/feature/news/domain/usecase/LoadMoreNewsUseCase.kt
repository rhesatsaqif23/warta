package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Fetch the next page of the latest feed into the local cache.
class LoadMoreNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(offset: Int) =
        repository.loadMoreNews(offset)
}
