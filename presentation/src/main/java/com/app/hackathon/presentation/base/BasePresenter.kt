package com.app.hackathon.presentation.base

abstract class BasePresenter: IBaseContract.IBasePresenter<BaseView> {
    private var view: BaseView? = null

    override fun onAttach(view: BaseView) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }
}