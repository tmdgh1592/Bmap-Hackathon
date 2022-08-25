package com.app.hackathon.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.hackathon.data.Constants
import com.app.hackathon.data.Constants.DB_NAME
import com.app.hackathon.data.db.dao.SearchHistoryDao
import com.app.hackathon.data.db.entity.SearchHistory

@Database(
    entities = [SearchHistory::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getHistoryDao(): SearchHistoryDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }

            return instance!!
        }
    }
}