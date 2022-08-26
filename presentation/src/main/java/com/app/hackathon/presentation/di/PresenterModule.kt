package com.app.hackathon.presentation.di

import com.app.hackathon.domain.usecase.*
import com.app.hackathon.presentation.presenter.map.MapPresenter
import com.app.hackathon.presentation.presenter.report.ReportPresenter
import com.app.hackathon.presentation.presenter.search.SearchPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object PresenterModule {

    @Provides
    fun provideMapPresenter(
        getAroundLotUseCase: GetAroundLotUseCase,
        filteredLotUseCase: GetFilteredLotUseCase
    ): MapPresenter {
        return MapPresenter(getAroundLotUseCase, filteredLotUseCase)
    }

    @Provides
    fun provideSearchPresenter(
        getSearchHistoryUseCase: GetSearchHistoryUseCase,
        addSearchHistoryUseCase: AddSearchHistoryUseCase,
        removeSearchHistoryUseCase: RemoveSearchHistoryUseCase,
        getParkingLotUseCase: GetParkingLotUseCase
    ): SearchPresenter {
        return SearchPresenter(
            getSearchHistoryUseCase,
            addSearchHistoryUseCase,
            removeSearchHistoryUseCase,
            getParkingLotUseCase
        )
    }

    @Provides
    fun provideReportPresenter(
        sendReportUseCase: SendReportUseCase
    ): ReportPresenter {
        return ReportPresenter(
            sendReportUseCase
        )
    }

}