package com.app.hackathon.data.di

import android.content.Context
import com.app.hackathon.data.db.AppDatabase
import com.app.hackathon.data.db.dao.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideHistoryDao(appDatabase: AppDatabase): SearchHistoryDao =
        appDatabase.getHistoryDao()
}