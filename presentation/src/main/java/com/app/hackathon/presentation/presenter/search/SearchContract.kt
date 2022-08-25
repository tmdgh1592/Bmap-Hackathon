package com.app.hackathon.presentation.presenter.search

import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.presentation.base.BaseContract

interface SearchContract {
    interface View: BaseContract.BaseView {
        fun showLoading(isShow: Boolean)
        fun showSearchHistory(searchHistoryList: List<SearchHistoryEntity>)
        fun finishWithResult(lotName: String)
        fun justFinish()
        fun removeSearchHistory(searchHistoryEntity: SearchHistoryEntity)
    }

    interface Presenter<T: BaseContract.BaseView>: BaseContract.BasePresenter<T> {
        fun requestSearchHistory()
        fun requestSaveHistory(lotName: String)
        fun requestRemoveHistory(history: SearchHistoryEntity)
    }
}