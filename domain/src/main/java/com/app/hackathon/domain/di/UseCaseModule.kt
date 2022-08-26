package com.app.hackathon.domain.di

import com.app.hackathon.domain.repository.ParkingLotRepository
import com.app.hackathon.domain.repository.ReportRepository
import com.app.hackathon.domain.repository.SearchHistoryRepository
import com.app.hackathon.domain.usecase.*
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

    @Provides
    @Singleton
    fun provideAddSearchHistoryUseCase(repository: SearchHistoryRepository): AddSearchHistoryUseCase {
        return AddSearchHistoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveSearchHistoryUseCase(repository: SearchHistoryRepository): RemoveSearchHistoryUseCase {
        return RemoveSearchHistoryUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetParkingLotUseCase(repository: ParkingLotRepository): GetParkingLotUseCase {
        return GetParkingLotUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAroundLotUseCase(repository: ParkingLotRepository): GetAroundLotUseCase {
        return GetAroundLotUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSendReportUseCase(repository: ReportRepository): SendReportUseCase {
        return SendReportUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFilteredLotUseCase(repository: ParkingLotRepository): GetFilteredLotUseCase {
        return GetFilteredLotUseCase(repository)
    }

}