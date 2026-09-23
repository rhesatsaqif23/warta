package com.rhesdev.warta.feature.news.data.repository

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
                val response = api.getNews()
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (e: Exception) {
                // Room cache remains as fallback
            }
        }
    }

    override suspend fun searchAndRefresh(query: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.searchNews(query)
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (e: Exception) {
                // Room cache remains as fallback
            }
        }
    }
}
