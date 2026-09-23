package com.rhesdev.warta.feature.news.data.remote

import com.rhesdev.warta.feature.news.data.remote.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

// News API endpoints.
interface NewsApi {

    @GET("search")
    suspend fun getNews(
        @Query("country") country: String = "ID",
        @Query("lang") lang: String = "id",
        @Query("size") size: Int = 30,
        @Query("sort") sort: String = "date"
    ): NewsResponse

    @GET("search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("country") country: String = "ID",
        @Query("lang") lang: String = "id",
        @Query("size") size: Int = 30
    ): NewsResponse
}
