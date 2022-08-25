package com.app.hackathon.domain.repository

import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface ParkingLotRepository {
    fun getLotList(query: String, latitude: String, longitude: String): Single<List<LotEntity>>
}