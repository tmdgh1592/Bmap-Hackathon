package com.app.hackathon.data.source.remote.lot

import com.app.hackathon.data.response.LotRequest
import com.app.hackathon.data.response.LotResponse
import io.reactivex.rxjava3.core.Single

interface ParkingLotLocalDataSource {
    fun getParkingLotList(lotRequest: LotRequest): Single<List<LotResponse>>
}