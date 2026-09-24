package com.rhesdev.warta.feature.news.data.repository

import com.rhesdev.warta.feature.news.data.local.NewsDao
import com.rhesdev.warta.feature.news.data.mapper.toContent
import com.rhesdev.warta.feature.news.data.mapper.toDomain
import com.rhesdev.warta.feature.news.data.mapper.toEntity
import com.rhesdev.warta.feature.news.data.remote.NewsApi
import com.rhesdev.warta.feature.news.domain.model.News
import com.rhesdev.warta.feature.news.domain.model.NewsStats
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
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

    override suspend fun getArticleBody(link: String): String? {
        return withContext(Dispatchers.IO) {
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
        withContext(Dispatchers.IO) {
            try {
                val response = api.getNews()
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { }
        }
    }

    override suspend fun refreshNewsByCategory(query: String, category: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.searchNews(query, date = "48h")
                dao.insertAll(response.results.map { it.toEntity().copy(category = category) })
            } catch (_: Exception) { }
        }
    }

    override suspend fun refreshTodayNews() {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getNews(date = "today")
                dao.insertAll(response.results.map { it.toEntity().copy(category = "today") })
            } catch (_: Exception) { }
        }
    }

    override suspend fun refreshNewsByHost(host: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getNewsByHost(host)
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { }
        }
    }

    override suspend fun loadMoreNews(offset: Int) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getNews(offset = offset)
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { }
        }
    }

    override suspend fun refreshNewsByDay(day: String) {
        withContext(Dispatchers.IO) {
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
        return withContext(Dispatchers.IO) {
            try {
                api.getStats().toDomain()
            } catch (_: Exception) {
                NewsStats()
            }
        }
    }

    override suspend fun getSearchStats(query: String): NewsStats {
        return withContext(Dispatchers.IO) {
            try {
                api.getStats(q = query).toDomain()
            } catch (_: Exception) {
                NewsStats()
            }
        }
    }

    override suspend fun searchAndRefresh(query: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = api.searchNews(query)
                dao.insertAll(response.results.map { it.toEntity() })
            } catch (_: Exception) { }
        }
    }
}
