package com.rhesdev.warta.feature.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Room database providing access to news table.
@Database(entities = [NewsEntity::class], version = 2, exportSchema = false)
abstract class WartaDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE news_table ADD COLUMN content TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
