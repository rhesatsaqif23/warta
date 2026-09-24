package com.rhesdev.warta.feature.news.domain.usecase

import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import javax.inject.Inject

// Load one article's full body text, cached offline when available.
class GetArticleBodyUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(link: String): String? =
        repository.getArticleBody(link)
}
