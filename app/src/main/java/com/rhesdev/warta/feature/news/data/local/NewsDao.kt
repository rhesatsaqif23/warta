package com.rhesdev.warta.feature.news.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Room DAO for cached news.
@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table ORDER BY isoDate DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table WHERE title LIKE '%' || :query || '%' ORDER BY isoDate DESC")
    fun searchNews(query: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table WHERE link = :link")
    suspend fun getNewsByLink(link: String): NewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)
}
