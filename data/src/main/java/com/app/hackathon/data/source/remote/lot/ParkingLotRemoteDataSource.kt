package com.app.hackathon.data.source.remote.lot

import com.app.hackathon.data.model.AroundLotRequest
import com.app.hackathon.data.model.LocationFilterOptionRequest
import com.app.hackathon.data.model.LotRequest
import com.app.hackathon.data.model.LotResponse
import io.reactivex.rxjava3.core.Single

interface ParkingLotRemoteDataSource {
    fun getParkingLotList(lotRequest: LotRequest): Single<List<LotResponse>>
    fun getAroundLotList(aroundLotRequest: AroundLotRequest): Single<List<LotResponse>>
    fun getFilteredLotList(locationFilterOptionRequest: LocationFilterOptionRequest): Single<List<LotResponse>>
}