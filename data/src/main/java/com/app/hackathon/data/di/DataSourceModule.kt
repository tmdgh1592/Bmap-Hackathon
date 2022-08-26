package com.app.hackathon.data.di

import com.app.hackathon.data.source.local.searchhistory.SearchHistoryLocalDataSource
import com.app.hackathon.data.source.local.searchhistory.SearchHistoryLocalDataSourceImpl
import com.app.hackathon.data.source.remote.lot.ParkingLotRemoteDataSource
import com.app.hackathon.data.source.remote.lot.ParkingLotRemoteDataSourceImpl
import com.app.hackathon.data.source.remote.report.ReportRemoteDataSource
import com.app.hackathon.data.source.remote.report.ReportRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindSearchHistoryLocalDataSource(
        searchHistoryLocalDataSourceImpl: SearchHistoryLocalDataSourceImpl
    ): SearchHistoryLocalDataSource

    @Singleton
    @Binds
    abstract fun bindParkingLotRemoteDataSource(
        parkingLotLocalDataSourceImpl: ParkingLotRemoteDataSourceImpl
    ): ParkingLotRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindReportRemoteDataSource(
        reportRemoteDataSourceImpl: ReportRemoteDataSourceImpl
    ): ReportRemoteDataSource
}