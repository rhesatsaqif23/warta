package com.rhesdev.warta.feature.news.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Room database providing access to news table. */
@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class WartaDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: WartaDatabase? = null

        fun getDatabase(context: Context): WartaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WartaDatabase::class.java,
                    "warta_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
