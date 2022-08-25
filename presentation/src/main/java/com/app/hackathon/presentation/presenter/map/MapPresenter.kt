package com.app.hackathon.presentation.presenter.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.app.hackathon.presentation.view.map.MapMode
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MapPresenter: MapContract.Presenter<MapContract.View> {

    private var view: MapContract.View? = null
    private val disposables = CompositeDisposable()
    var currentLat: Double = 0.0
        private set
    var currentLng: Double = 0.0
        private set

    private val _currentMapMode = MutableLiveData<MapMode>(MapMode.INACTIVE)
    val currentMapMode = _currentMapMode

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
}