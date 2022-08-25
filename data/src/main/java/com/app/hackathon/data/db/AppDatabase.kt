package com.app.hackathon.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.hackathon.data.Constants.DB_NAME
import com.app.hackathon.data.db.dao.SearchHistoryDao
import com.app.hackathon.data.db.entity.SearchHistoryEntity

@Database(
    entities = [SearchHistoryEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getHistoryDao(): SearchHistoryDao

    companion object {
        private const val databaseName = DB_NAME

        fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
    }
}