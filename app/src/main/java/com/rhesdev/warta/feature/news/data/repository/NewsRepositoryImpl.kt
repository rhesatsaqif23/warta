package com.rhesdev.warta.feature.news.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rhesdev.warta.core.di.IoDispatcher
import com.rhesdev.warta.feature.news.data.local.NewsDao
import com.rhesdev.warta.feature.news.data.mapper.toContent
import com.rhesdev.warta.feature.news.data.mapper.toContent
import com.rhesdev.warta.feature.news.data.mapper.toDomain
import com.rhesdev.warta.feature.news.data.mapper.toEntity
import com.rhesdev.warta.feature.news.data.remote.NewsApi
import com.rhesdev.warta.feature.news.data.remote.NewsRemoteMediator
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.model.NewsStats
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

private val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

// Room + API repository implementing the domain contract.
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao,
    private val remoteMediator: NewsRemoteMediator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {

    override fun getTopNews(): Flow<List<News>> {
        return dao.getAllNews().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getHomeFeed(): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 8,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { dao.pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun searchNews(query: String): Flow<List<News>> {
        return dao.searchNews(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getNewsByLink(link: String): News? {
        return withContext(ioDispatcher) {
            dao.getNewsByLink(link)?.toDomain()
        }
    }

    override suspend fun getArticleBody(link: String): String? {
        return withContext(ioDispatcher) {
            dao.getNewsByLink(link)?.content?.takeIf { it.isNotBlank() }?.let { return@withContext it }
            try {
                val body = api.getArticle(link).toContent()
                if (body.isBlank()) return@withContext null
                dao.updateContent(link, body)
                body
            } catch (_: Exception) {
                null
            }
        }
    }

    override suspend fun refreshNews() {
        withContext(ioDispatcher) {
            try {
                val response = api.getNews()
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { }
        }
    }

    override suspend fun refreshNewsByCategory(query: String, category: String) {
        withContext(ioDispatcher) {
            try {
                val response = api.searchNews(query, date = "48h")
                dao.insertAll(response.results.map { it.toEntity().copy(category = category) })
            } catch (_: Exception) { }
        }
    }

    override suspend fun refreshNewsByDay(day: String) {
        withContext(ioDispatcher) {
            try {
                val parsed = dayFormat.parse(day) ?: return@withContext
                val next = dayFormat.format(
                    Calendar.getInstance().apply {
                        time = parsed
                        add(Calendar.DAY_OF_MONTH, 1)
                    }.time
                )
                val response = api.getNewsByDateRange(from = day, to = next)
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { }
        }
    }

    override suspend fun getTrendStats(): NewsStats {
        return withContext(ioDispatcher) {
            try {
                api.getStats().toDomain()
            } catch (_: Exception) {
                NewsStats()
            }
        }
    }

    override suspend fun getSearchStats(query: String): NewsStats {
        return withContext(ioDispatcher) {
            try {
                api.getStats(q = query).toDomain()
            } catch (_: Exception) {
                NewsStats()
            }
        }
    }

    override suspend fun searchAndRefresh(query: String) {
        withContext(ioDispatcher) {
            try {
                val response = api.searchNews(query)
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { }
        }
    }
}
