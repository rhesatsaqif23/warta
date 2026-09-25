package com.rhesdev.warta.feature.news.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rhesdev.warta.feature.news.data.local.NewsDao
import com.rhesdev.warta.feature.news.data.local.NewsEntity
import com.rhesdev.warta.feature.news.data.mapper.toEntity
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

private const val MEDIATOR_PAGE_SIZE = 100

// Pulls the latest feed in offset pages and caches it into Room.
@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao
) : RemoteMediator<Int, NewsEntity>() {

    private var nextOffset = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, NewsEntity>): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> nextOffset
        }
        return try {
            val results = api.getNews(offset = offset).results
            dao.insertAll(results.map { it.toEntity() })
            nextOffset = offset + results.size
            MediatorResult.Success(endOfPaginationReached = results.size < MEDIATOR_PAGE_SIZE)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}