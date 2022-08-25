package com.app.hackathon.presentation.presenter.search

import android.util.Log
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.domain.usecase.AddSearchHistoryUseCase
import com.app.hackathon.domain.usecase.GetParkingLotUseCase
import com.app.hackathon.domain.usecase.GetSearchHistoryUseCase
import com.app.hackathon.domain.usecase.RemoveSearchHistoryUseCase
import com.app.hackathon.presentation.widget.extensions.convertToLotEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addSearchHistoryUseCase: AddSearchHistoryUseCase,
    private val removeSearchHistoryUseCase: RemoveSearchHistoryUseCase,
    private val getParkingLotUseCase: GetParkingLotUseCase
) : SearchContract.Presenter<SearchContract.View> {


    private var view: SearchContract.View? = null
    private val disposables = CompositeDisposable()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val querySubject: PublishSubject<String> = PublishSubject.create()

    init {
        setQuerySubject()
    }


    override fun onAttach(view: SearchContract.View) {
        this.view = view
    }

    override fun onDetach() {
        view = null
        disposables.clear()
    }

    override fun setLatLng(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
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


    override fun requestSaveHistory(searchHistory: SearchHistoryEntity) {

        disposables.add(
            Completable.fromAction {
                addSearchHistoryUseCase(searchHistory)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        view?.finishWithSearchResult(searchHistory.convertToLotEntity())
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

    // View가 Presenter에게 주차장 리스트 요청
    override fun requestLotList(query: String) {
        // 두 글자 미만이면 검색 히스토리를 보여준다
        if (query.length < 2)
            view?.showSearchHistoryList()

        querySubject.onNext(query)
    }


    // 쿼리 Subject 를 설정한다.
    private fun setQuerySubject() {
        disposables.add(querySubject.debounce(300, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { query ->
                view?.showLoading(true) // EditText에 입력값이 들어오면 로딩화면을 보여줌
                loadLotList(query) // 주차장 데이터를 불러온다.
            }
            .doOnComplete {
                view?.showLoading(false)
            }.doOnError {
                view?.showLoading(false)
                view?.showErrorToast("GPS가 설정되어 있지 않아요!")
            }.subscribe())
    }


    // 서버로부터 주차장 리스트를 불러온다.
    private fun loadLotList(query: String) {
        if (latitude == 0.0 && longitude == 0.0) {
            // 위도 경도가 설정되어 있지 않으면 에러처리
            querySubject.onError(Exception("Latitude and Longitude are not set yet."))
        }

        disposables.add(
            getParkingLotUseCase(query, latitude.toString(), longitude.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<LotEntity>>() {
                    override fun onSuccess(searchResultList: List<LotEntity>) {
                        view?.showLoading(false)
                        view?.refreshSearchResult(searchResultList) // 데이터를 불러오면 View의 검색 결과 갱신
                        view?.showSearchResultList() // 검색 결과 리사이클러뷰를 보여준다
                    }

                    override fun onError(e: Throwable) {
                        view?.showLoading(false)
                        view?.refreshSearchResult(emptyList())
                        view?.showSearchHistoryList() // 검색 히스토리 리사이클러뷰를 보여준다
                    }
                })
        )
    }

}