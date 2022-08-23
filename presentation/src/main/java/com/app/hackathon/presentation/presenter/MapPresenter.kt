package com.app.hackathon.presentation.presenter

import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MapPresenter constructor(

) : MapContract.Presenter<MapContract.View> {

    private var view: MapContract.View? = null
    private val disposables = CompositeDisposable()

    override fun onAttach(view: MapContract.View) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }


}