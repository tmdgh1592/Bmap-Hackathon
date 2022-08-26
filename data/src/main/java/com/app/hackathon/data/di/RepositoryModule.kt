package com.app.hackathon.data.di

import com.app.hackathon.data.repository.ParkingLotRepositoryImpl
import com.app.hackathon.data.repository.ReportRepositoryImpl
import com.app.hackathon.data.repository.SearchHistoryRepositoryImpl
import com.app.hackathon.domain.repository.ParkingLotRepository
import com.app.hackathon.domain.repository.ReportRepository
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

    @Binds
    @Singleton
    abstract fun bindParkingLotRepository(parkingLotRepositoryImpl: ParkingLotRepositoryImpl): ParkingLotRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(reportRepositoryImpl: ReportRepositoryImpl): ReportRepository

}