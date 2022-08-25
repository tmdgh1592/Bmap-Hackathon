package com.app.hackathon.presentation.presenter.search

import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.presentation.base.BaseContract

interface SearchContract {
    interface View: BaseContract.BaseView {
        fun showLoading(isShow: Boolean)
        fun showSearchHistory(searchHistoryList: List<SearchHistoryEntity>)
        fun finishWithSearchResult(lot: LotEntity)
        fun justFinish()
        fun removeSearchHistory(searchHistoryEntity: SearchHistoryEntity)
        fun refreshSearchResult(lot: List<LotEntity>)
        fun showSearchHistoryList()
        fun showSearchResultList()
        fun showErrorToast(errorMessage: String)
    }

    interface Presenter<T: BaseContract.BaseView>: BaseContract.BasePresenter<T> {
        fun requestSearchHistory()
        fun requestSaveHistory(searchHistory: SearchHistoryEntity)
        fun requestRemoveHistory(history: SearchHistoryEntity)
        fun requestLotList(query: String)
        fun setLatLng(latitude: Double, longitude: Double)
    }
}