package com.app.hackathon.presentation.presenter.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.hackathon.domain.entity.FilterOption
import com.app.hackathon.domain.entity.LocationFilterOptionEntity
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.usecase.GetAroundLotUseCase
import com.app.hackathon.domain.usecase.GetFilteredLotUseCase
import com.app.hackathon.presentation.view.map.MapMode
import com.app.hackathon.presentation.widget.extensions.toBoolean
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MapPresenter @Inject constructor(
    private val getAroundLotUseCase: GetAroundLotUseCase,
    private val getFilteredLotUseCase: GetFilteredLotUseCase
) : MapContract.Presenter<MapContract.View> {

    private var view: MapContract.View? = null
    private val disposables = CompositeDisposable()
    var currentLat: Double = 126.9898441
        private set
    var currentLng: Double = 37.5685262
        private set

    var lookingLat: Double = 126.9898441
        private set
    var lookingLng: Double = 37.5685262
        private set

    //    var currentLat: Double = 0.0F
//        private set
//    var currentLng: Double = 0.0
//        private set
    var selectedLot: LotEntity? = null
        private set

    // 클릭해서 이동하고 있는지 여부
    var isClickMoving = false


    private val _currentMapMode = MutableLiveData<MapMode>(MapMode.INACTIVE)
    val currentMapMode = _currentMapMode

    val filterOptions: ArrayList<FilterOption> = arrayListOf(
        FilterOption("엘리베이터", -1),
        FilterOption("넓은 출입구", -1),
        FilterOption("경사로", -1),
        FilterOption("접근로", -1),
        FilterOption("휠체어 리프트", -1),
        FilterOption("점자블록", -1),
        FilterOption("시각장애 유도 안내", -1),
        FilterOption("전용매표소", -1),
        FilterOption("장애인 전용 화장실", -1)
    )

    override fun onAttach(view: MapContract.View) {
        this.view = view
    }

    override fun onDetach() {
        view = null
        disposables.clear()
    }

    fun setMapMode(newMapMode: MapMode) {
        _currentMapMode.value = newMapMode
    }

    fun changeNextMapMode() {
        _currentMapMode.value = currentMapMode.value?.changeMode()
    }

    fun setInitialMapMode() {
        _currentMapMode.value = MapMode.INACTIVE
    }

    fun updateCurrentLocation(newLocation: Location) {
        currentLat = newLocation.latitude
        currentLng = newLocation.longitude
    }

    fun updateLookingLocation(newLat: Double, newLng: Double) {
        lookingLat = newLat
        lookingLat = newLng
    }

    fun updateFilterOptions(newFilterOption: List<FilterOption>) {
        filterOptions.clear()
        filterOptions.addAll(newFilterOption)
    }

    override fun requestAroundLotsByLocation(
        latitude: Double,
        longitude: Double
    ) {
        disposables.add(
            getAroundLotUseCase(
                latitude.toString(),
                longitude.toString()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<LotEntity>>() {
                    override fun onSuccess(searchResultList: List<LotEntity>) {
                        // Log.d("TAG", "onSuccess: $searchResultList")
                        view?.showLotsOnMap(searchResultList)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TAG", "onError: " + "주변 주차장 데이터 가져오기 실패")
                    }
                })
        )
    }

    override fun selectLot(lot: LotEntity) {
        selectedLot = lot
    }

    // 필터 적용시
    override fun requestFilteredLotsByLocation(latitude: Double, longitude: Double) {
        val entity = LocationFilterOptionEntity(
            filterOptions[0].isChecked.toBoolean(),
            filterOptions[1].isChecked.toBoolean(),
            filterOptions[2].isChecked.toBoolean(),
            filterOptions[3].isChecked.toBoolean(),
            filterOptions[4].isChecked.toBoolean(),
            filterOptions[5].isChecked.toBoolean(),
            filterOptions[6].isChecked.toBoolean(),
            filterOptions[7].isChecked.toBoolean(),
            filterOptions[8].isChecked.toBoolean(),
            latitude.toString(),
            longitude.toString()
        )

        disposables.add(
            getFilteredLotUseCase(
                entity
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<LotEntity>>() {
                    override fun onSuccess(searchResultList: List<LotEntity>) {
                         Log.d("TAG", "onSuccessFilter: $searchResultList")
                        // 필터링된 주차장 마커를 맵에 띄우기
                        view?.showLotsOnMap(searchResultList)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TAG", "onError: " + "필터링된 주차장 데이터 가져오기 실패")
                    }
                })
        )

    }

    fun isFiltered(): Boolean {
        var isFiltered = false

        filterOptions.forEach {
            if (it.isChecked.toBoolean()) {
                isFiltered = true
                return@forEach
            }
        }

        return isFiltered
    }

}