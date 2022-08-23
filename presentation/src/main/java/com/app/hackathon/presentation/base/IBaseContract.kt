package com.app.hackathon.presentation.base

interface IBaseContract {

    interface IBaseView {

    }

    interface IBasePresenter<T: IBaseView> {
        fun onAttach(view: T)
        fun onDetach()
    }
}