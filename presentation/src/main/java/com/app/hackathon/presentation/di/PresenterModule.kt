package com.app.hackathon.presentation.di

import com.app.hackathon.presentation.presenter.MapPresenter
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

}