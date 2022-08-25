package com.app.hackathon.presentation.presenter.search

import android.util.Log
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.domain.usecase.AddSearchHistoryUseCase
import com.app.hackathon.domain.usecase.GetSearchHistoryUseCase
import com.app.hackathon.domain.usecase.RemoveSearchHistoryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addSearchHistoryUseCase: AddSearchHistoryUseCase,
    private val removeSearchHistoryUseCase: RemoveSearchHistoryUseCase
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

        disposables.add(
            getSearchHistoryUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<SearchHistoryEntity>>() {
                    override fun onSuccess(searchHistories: List<SearchHistoryEntity>) {
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

    override fun requestSaveHistory(lotName: String) {
        val searchHistory = SearchHistoryEntity(
            lotName = lotName
        )

        disposables.add(
            Completable.fromAction {
                addSearchHistoryUseCase(searchHistory)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        view?.finishWithResult(lotName)
                    },
                    {
                        Log.d("SearchPresenter", "requestSaveHistory: ${it.message}")
                        view?.justFinish()
                    }
                ))

    }

    override fun requestRemoveHistory(history: SearchHistoryEntity) {
        disposables.add(
            Completable.fromAction {
                removeSearchHistoryUseCase(history)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        // 리사이클러뷰에서 해당 기록 삭제
                        view?.removeSearchHistory(history)
                    },
                    {
                        Log.d("SearchPresenter", "requestSaveHistory: ${it.message}")
                        // Do Nothing
                    }
                )
        )

    }

}