package com.app.hackathon.data.source.remote.lot

import com.app.hackathon.data.api.LotApiService
import com.app.hackathon.data.response.LotRequest
import com.app.hackathon.data.response.LotResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ParkingLotLocalDataSourceImpl @Inject constructor(
    private val parkingLotApi: LotApiService
) : ParkingLotLocalDataSource {
    override fun getParkingLotList(lotRequest: LotRequest): Single<List<LotResponse>> {
        return parkingLotApi.loadLotQuery(lotRequest)
    }
}