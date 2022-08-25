package com.app.hackathon.presentation.di

import com.app.hackathon.domain.usecase.GetSearchHistoryUseCase
import com.app.hackathon.presentation.presenter.map.MapPresenter
import com.app.hackathon.presentation.presenter.search.SearchPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object PresenterModule {

    @Provides
    fun provideMapPresenter(): MapPresenter {
        return MapPresenter()
    }

    @Provides
    fun provideSearchPresenter(getSearchHistoryUseCase: GetSearchHistoryUseCase): SearchPresenter {
        return SearchPresenter(getSearchHistoryUseCase)
    }

}