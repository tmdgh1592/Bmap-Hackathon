package com.app.hackathon.domain.di

import com.app.hackathon.domain.repository.SearchHistoryRepository
import com.app.hackathon.domain.usecase.GetSearchHistoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetSearchHistoryUseCase(repository: SearchHistoryRepository): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCase(repository)
    }

}