package com.rhesdev.warta.core.di

import android.content.Context
import androidx.room.Room
import com.rhesdev.warta.core.data.local.WartaDatabase
import com.rhesdev.warta.core.data.local.dao.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt module providing database dependencies. */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
