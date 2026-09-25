package com.rhesdev.warta.feature.news.domain.repository

import androidx.paging.PagingData
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.model.NewsStats
import kotlinx.coroutines.flow.Flow

// News data access contract.
interface NewsRepository {

    fun getTopNews(): Flow<List<News>>

    fun getHomeFeed(): Flow<PagingData<News>>

    fun searchNews(query: String): Flow<List<News>>

    suspend fun getNewsByLink(link: String): News?

    suspend fun getArticleBody(link: String): String?

    suspend fun refreshNews()

    suspend fun refreshNewsByCategory(query: String, category: String)

    suspend fun refreshNewsByDay(day: String)

    suspend fun getTrendStats(): NewsStats

    suspend fun getSearchStats(query: String): NewsStats

    suspend fun searchAndRefresh(query: String)
}
