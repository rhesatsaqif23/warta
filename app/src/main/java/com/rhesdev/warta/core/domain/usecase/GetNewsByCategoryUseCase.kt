package com.rhesdev.warta.core.domain.usecase

import com.rhesdev.warta.core.domain.model.News
import com.rhesdev.warta.core.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Use case to get news filtered by category. */
class GetNewsByCategoryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(category: String): Flow<List<News>> =
        repository.getNewsByCategory(category)
}
