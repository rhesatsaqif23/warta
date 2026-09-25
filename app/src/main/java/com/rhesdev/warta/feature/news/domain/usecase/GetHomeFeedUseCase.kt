package com.rhesdev.warta.feature.news.domain.usecase

import androidx.paging.PagingData
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Stream of paged news backing the Home feed.
class GetHomeFeedUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<PagingData<News>> = repository.getHomeFeed()
}