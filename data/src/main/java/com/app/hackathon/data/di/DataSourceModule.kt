package com.app.hackathon.data.di

import com.app.hackathon.data.source.local.searchhistory.SearchHistoryLocalDataSource
import com.app.hackathon.data.source.local.searchhistory.SearchHistoryLocalDataSourceImpl
import com.app.hackathon.data.source.remote.lot.ParkingLotLocalDataSource
import com.app.hackathon.data.source.remote.lot.ParkingLotLocalDataSourceImpl
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
    abstract fun bindParkingLotLocalDataSource(
        parkingLotLocalDataSourceImpl: ParkingLotLocalDataSourceImpl
    ): ParkingLotLocalDataSource
}