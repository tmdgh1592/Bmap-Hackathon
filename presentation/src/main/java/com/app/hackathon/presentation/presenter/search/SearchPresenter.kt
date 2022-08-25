package com.app.hackathon.presentation.presenter.search

import com.app.hackathon.domain.model.SearchHistory
import com.app.hackathon.domain.usecase.GetSearchHistoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    val getSearchHistoryUseCase: GetSearchHistoryUseCase
) : SearchContract.Presenter<SearchContract.View> {

    private var view: SearchContract.View? = null
    private val disposables = CompositeDisposable()

    override fun onAttach(view: SearchContract.View) {
        this.view = view
    }

    override fun onDetach() {
        view = null
        disposables.clear()
    }

    // 검색 히스토리를 요청한다.
    override fun requestSearchHistory() {
        view?.showLoading(true)

        disposables.add(getSearchHistoryUseCase()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<SearchHistory>>() {
                override fun onSuccess(searchHistories: List<SearchHistory>) {
                    view?.showLoading(false)
                    view?.showSearchHistory(searchHistories)
                }

                override fun onError(e: Throwable) {
                    view?.showLoading(false)
                    view?.showSearchHistory(emptyList())
                }
            })
        )

    }

}