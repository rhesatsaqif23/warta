package com.rhesdev.warta.core.data.repository

import com.rhesdev.warta.core.data.local.dao.NewsDao
import com.rhesdev.warta.core.data.mapper.toDomain
import com.rhesdev.warta.core.data.mapper.toEntity
import com.rhesdev.warta.core.data.remote.NewsApi
import com.rhesdev.warta.core.domain.model.News
import com.rhesdev.warta.core.domain.repository.NewsRepository
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

    override suspend fun refreshNews(source: String, category: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = if (category.isEmpty()) {
                    api.getNews(source)
                } else {
                    api.getNewsByType(source, category)
                }
                val entities = response.data.map { dto ->
                    dto.toEntity(source = source, category = category)
                }
                dao.insertAll(entities)
            } catch (e: Exception) {
                // Room cache remains as fallback
            }
        }
    }
}
