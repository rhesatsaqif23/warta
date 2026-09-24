package com.rhesdev.warta.core.di

import android.content.Context
import androidx.room.Room
import com.rhesdev.warta.feature.news.data.local.NewsDao
import com.rhesdev.warta.feature.news.data.local.WartaDatabase
import com.rhesdev.warta.feature.news.data.remote.NewsApi
import com.rhesdev.warta.feature.news.data.repository.NewsRepositoryImpl
import com.rhesdev.warta.feature.news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Hilt module for news feature dependencies.
@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    private const val BASE_URL = "https://freenewsapi.ai/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WartaDatabase {
        return Room.databaseBuilder(
            context,
            WartaDatabase::class.java,
            "warta_database"
        ).build()
    }

    @Provides
    fun provideNewsDao(database: WartaDatabase): NewsDao {
        return database.newsDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NewsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository
}
