package com.rhesdev.warta.core.data.remote

import com.rhesdev.warta.core.data.remote.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {

    @GET("{source}")
    suspend fun getNews(
        @Path("source") source: String
    ): NewsResponse

    @GET("{source}/{type}")
    suspend fun getNewsByType(
        @Path("source") source: String,
        @Path("type") type: String
    ): NewsResponse

    @GET("{source}/{type}")
    suspend fun searchNews(
        @Path("source") source: String,
        @Path("type") type: String,
        @Query("search") query: String
    ): NewsResponse
}
