package com.app.hackathon.data.repository

import com.app.hackathon.data.mapper.LotMapper.mapToDomainModel
import com.app.hackathon.data.response.LotRequest
import com.app.hackathon.data.source.remote.lot.ParkingLotLocalDataSource
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.repository.ParkingLotRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ParkingLotRepositoryImpl @Inject constructor(
    private val parkingLotLocalDataSource: ParkingLotLocalDataSource
) : ParkingLotRepository {
    override fun getLotList(
        query: String,
        latitude: String,
        longitude: String
    ): Single<List<LotEntity>> {
        return parkingLotLocalDataSource.getParkingLotList(
            LotRequest(query, latitude, longitude)
        ).map { lotList ->
            lotList.map { lot ->
                lot.mapToDomainModel()
            }
        }
    }

}