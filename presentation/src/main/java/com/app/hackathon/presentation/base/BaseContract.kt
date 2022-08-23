package com.app.hackathon.presentation.base

import io.reactivex.rxjava3.disposables.CompositeDisposable

interface BaseContract {
    interface BaseView {

    }
    interface BasePresenter<T: BaseView> {
        fun onAttach(view: T)
        fun onDetach()
    }
}