package com.rhesdev.warta.core.domain.usecase

import com.rhesdev.warta.core.domain.repository.NewsRepository
import javax.inject.Inject

/** Use case to refresh news from API and store in database. */
class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(source: String, category: String) =
        repository.refreshNews(source, category)
}
