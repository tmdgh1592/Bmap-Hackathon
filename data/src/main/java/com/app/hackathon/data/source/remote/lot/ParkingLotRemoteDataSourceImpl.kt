package com.app.hackathon.data.source.remote.lot

import com.app.hackathon.data.api.LotApiService
import com.app.hackathon.data.model.AroundLotRequest
import com.app.hackathon.data.model.LocationFilterOptionRequest
import com.app.hackathon.data.model.LotRequest
import com.app.hackathon.data.model.LotResponse
import com.app.hackathon.domain.entity.LocationFilterOptionEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ParkingLotRemoteDataSourceImpl @Inject constructor(
    private val parkingLotApi: LotApiService
) : ParkingLotRemoteDataSource {
    override fun getParkingLotList(lotRequest: LotRequest): Single<List<LotResponse>> {
//        return parkingLotApi.loadLotQuery(lotRequest.address, lotRequest.latitude, lotRequest.longitude)
        return parkingLotApi.loadLotQuery(lotRequest)
    }

    override fun getAroundLotList(aroundLotRequest: AroundLotRequest): Single<List<LotResponse>> {
//        return parkingLotApi.loadAroundLot(aroundLotRequest.latitude, aroundLotRequest.longitude)
        return parkingLotApi.loadAroundLot(aroundLotRequest)
    }

    override fun getFilteredLotList(locationFilterOptionRequest: LocationFilterOptionRequest): Single<List<LotResponse>> {
        return parkingLotApi.loadFilteredLot(locationFilterOptionRequest)
    }
}