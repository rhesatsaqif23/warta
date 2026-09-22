package com.rhesdev.warta.core.di

import com.rhesdev.warta.core.data.remote.NewsApi
import com.rhesdev.warta.core.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Hilt module providing network dependencies. */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi = RetrofitClient.api
}
