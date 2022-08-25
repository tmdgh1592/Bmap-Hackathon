package com.app.hackathon.data.di

import com.app.hackathon.data.repository.SearchHistoryRepositoryImpl
import com.app.hackathon.domain.repository.SearchHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSearchHistoryRepository(searchHistoryRepository: SearchHistoryRepositoryImpl): SearchHistoryRepository

}