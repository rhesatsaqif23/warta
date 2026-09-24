package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Fetch one calendar day's news and refresh the local cache.
class RefreshNewsByDayUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(day: String) =
        repository.refreshNewsByDay(day)
}
