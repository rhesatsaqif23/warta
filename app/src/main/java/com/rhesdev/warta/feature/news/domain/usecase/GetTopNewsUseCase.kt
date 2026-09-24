package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Use case to get all news sorted by date. */
class GetTopNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<News>> = repository.getTopNews()
}
