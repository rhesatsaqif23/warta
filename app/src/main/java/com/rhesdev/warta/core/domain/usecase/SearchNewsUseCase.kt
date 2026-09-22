package com.rhesdev.warta.core.domain.usecase

import com.rhesdev.warta.core.domain.model.News
import com.rhesdev.warta.core.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Use case to search news by title query. */
class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(query: String): Flow<List<News>> =
        repository.searchNews(query)
}
