package com.app.hackathon.presentation.base

abstract class BasePresenter: IBaseContract.IBasePresenter<BaseView> {
    protected var view: BaseView? = null
        private set

    override fun onAttach(view: BaseView) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }
}