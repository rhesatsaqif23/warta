package com.rhesdev.warta.feature.news.data.repository

import android.util.Log
import com.rhesdev.warta.feature.news.data.local.NewsDao
import com.rhesdev.warta.feature.news.data.mapper.toDomain
import com.rhesdev.warta.feature.news.data.mapper.toEntity
import com.rhesdev.warta.feature.news.data.remote.NewsApi
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "NewsRepositoryImpl"

/** Implementation of NewsRepository. Handles API + Room data access. */
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao
) : NewsRepository {

    override fun getTopNews(): Flow<List<News>> {
        return dao.getAllNews().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getNewsByCategory(category: String): Flow<List<News>> {
        return dao.getNewsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchNews(query: String): Flow<List<News>> {
        return dao.searchNews(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getNewsByLink(link: String): News? {
        return withContext(Dispatchers.IO) {
            dao.getNewsByLink(link)?.toDomain()
        }
    }

    override suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching news from freenewsapi.ai")
                val response = api.getNews()
                val entities = response.results.map { it.toEntity() }
                Log.d(TAG, "Received ${entities.size} articles, inserting to Room")
                dao.insertAll(entities)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to refresh news", e)
            }
        }
    }

    override suspend fun searchAndRefresh(query: String) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Searching news: $query")
                val response = api.searchNews(query)
                val entities = response.results.map { it.toEntity() }
                Log.d(TAG, "Search returned ${entities.size} articles")
                dao.insertAll(entities)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to search news", e)
            }
        }
    }
}
