package com.rhesdev.warta.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rhesdev.warta.core.data.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table ORDER BY isoDate DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table WHERE category = :category ORDER BY isoDate DESC")
    fun getNewsByCategory(category: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table WHERE title LIKE '%' || :query || '%' ORDER BY isoDate DESC")
    fun searchNews(query: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_table WHERE link = :link")
    suspend fun getNewsByLink(link: String): NewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)

    @Query("DELETE FROM news_table WHERE category = :category")
    suspend fun deleteByCategory(category: String)
}
