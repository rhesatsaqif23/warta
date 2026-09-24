package com.rhesdev.warta.feature.news.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rhesdev.warta.feature.news.domain.usecase.RefreshNewsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

// Periodic background refresh of the news cache.
@HiltWorker
class NewsRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            refreshNewsUseCase()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}
