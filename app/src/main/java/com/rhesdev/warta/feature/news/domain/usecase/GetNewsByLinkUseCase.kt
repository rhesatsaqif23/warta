package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

/** Use case to get a single news article by its link. */
class GetNewsByLinkUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(link: String): News? =
        repository.getNewsByLink(link)
}
