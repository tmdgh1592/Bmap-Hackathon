package com.app.hackathon.presentation.presenter

import com.app.hackathon.presentation.base.BaseContract

interface MapContract {
    interface View: BaseContract.BaseView {

    }
    interface Presenter<T: BaseContract.BaseView> : BaseContract.BasePresenter<T> {

    }
}