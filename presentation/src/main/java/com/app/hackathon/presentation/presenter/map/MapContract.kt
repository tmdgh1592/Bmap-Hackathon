package com.app.hackathon.presentation.presenter.map

import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.presentation.base.BaseContract
import io.reactivex.rxjava3.core.Single

interface MapContract {
    interface View: BaseContract.BaseView {
        fun showLotsOnMap(lotList: List<LotEntity>)
    }
    interface Presenter<T: BaseContract.BaseView> : BaseContract.BasePresenter<T> {
        fun requestAroundLotsByLocation(latitude: Double, longitude: Double)
        fun selectLot(lot: LotEntity)
        fun requestFilteredLotsByLocation(latitude: Double, longitude: Double)
    }
}