package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

/** Use case to search remote news and refresh the local cache. */
class SearchAndRefreshUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String) =
        repository.searchAndRefresh(query)
}
