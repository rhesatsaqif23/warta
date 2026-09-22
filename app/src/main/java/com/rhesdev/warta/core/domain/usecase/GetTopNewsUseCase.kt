package com.rhesdev.warta.core.domain.usecase

import com.rhesdev.warta.core.domain.model.News
import com.rhesdev.warta.core.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Use case to get all news sorted by date. */
class GetTopNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<News>> = repository.getTopNews()
}
