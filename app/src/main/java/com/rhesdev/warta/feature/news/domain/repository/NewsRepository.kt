package com.rhesdev.warta.feature.news.domain.repository

import com.rhesdev.warta.feature.news.domain.model.News
import kotlinx.coroutines.flow.Flow

/** Repository interface defining news data operations. */
interface NewsRepository {

    fun getTopNews(): Flow<List<News>>

    fun getNewsByCategory(category: String): Flow<List<News>>

    fun searchNews(query: String): Flow<List<News>>

    suspend fun getNewsByLink(link: String): News?

    suspend fun refreshNews()

    suspend fun searchAndRefresh(query: String)
}
