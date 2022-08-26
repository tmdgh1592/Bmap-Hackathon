package com.app.hackathon.domain.repository

import com.app.hackathon.domain.entity.LocationFilterOptionEntity
import com.app.hackathon.domain.entity.LotEntity
import io.reactivex.rxjava3.core.Single

interface ParkingLotRepository {
    fun getLotList(query: String, latitude: String, longitude: String): Single<List<LotEntity>>
    fun getAroundLotList(latitude: String, longitude: String): Single<List<LotEntity>>
    fun getFilteredLotList(locationFilterOptionEntity: LocationFilterOptionEntity): Single<List<LotEntity>>
}