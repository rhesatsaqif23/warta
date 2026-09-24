package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Fetch one category by keyword and refresh the local cache.
class RefreshNewsByCategoryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String, category: String) =
        repository.refreshNewsByCategory(query, category)
}
