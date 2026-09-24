package com.rhesdev.warta.feature.news.data.remote

import com.rhesdev.warta.feature.news.data.remote.dto.NewsResponse
import com.rhesdev.warta.feature.news.data.remote.dto.StatsResponse
import retrofit2.http.GET
import retrofit2.http.Query

// News API endpoints.
interface NewsApi {

    @GET("search")
    suspend fun getNews(
        @Query("country") country: String = "ID",
        @Query("lang") lang: String = "id",
        @Query("size") size: Int = 100,
        @Query("sort") sort: String = "date",
        @Query("date") date: String? = "48h",
        @Query("offset") offset: Int = 0
    ): NewsResponse

    @GET("search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("country") country: String = "ID",
        @Query("lang") lang: String = "id",
        @Query("size") size: Int = 30,
        @Query("sort") sort: String = "relevance",
        @Query("date") date: String? = null
    ): NewsResponse

    @GET("search")
    suspend fun getNewsByHost(
        @Query("host") host: String,
        @Query("country") country: String = "ID",
        @Query("lang") lang: String = "id",
        @Query("size") size: Int = 30,
        @Query("sort") sort: String = "date",
        @Query("date") date: String? = "48h"
    ): NewsResponse

    @GET("stats")
    suspend fun getStats(
        @Query("country") country: String = "ID",
        @Query("lang") lang: String = "id",
        @Query("date") date: String = "7d",
        @Query("top") top: Int = 8
    ): StatsResponse
}
