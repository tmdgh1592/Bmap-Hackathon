package com.app.hackathon.presentation.presenter.search

import com.app.hackathon.domain.model.SearchHistory
import com.app.hackathon.presentation.base.BaseContract

interface SearchContract {
    interface View: BaseContract.BaseView {
        fun showLoading(show: Boolean)

        fun showSearchHistory(searchHistoryList: List<SearchHistory>)
    }
    interface Presenter<T: BaseContract.BaseView>: BaseContract.BasePresenter<T> {
        fun requestSearchHistory()
    }
}