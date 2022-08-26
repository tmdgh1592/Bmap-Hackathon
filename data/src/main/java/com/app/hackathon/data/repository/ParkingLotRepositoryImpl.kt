package com.app.hackathon.data.repository

import com.app.hackathon.data.mapper.FilterOptionMapper.mapFromDomainModel
import com.app.hackathon.data.mapper.FilterOptionMapper.mapToDomainModel
import com.app.hackathon.data.mapper.LotMapper.mapToDomainModel
import com.app.hackathon.data.model.AroundLotRequest
import com.app.hackathon.data.model.LotRequest
import com.app.hackathon.data.source.remote.lot.ParkingLotRemoteDataSource
import com.app.hackathon.domain.entity.LocationFilterOptionEntity
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.repository.ParkingLotRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ParkingLotRepositoryImpl @Inject constructor(
    private val parkingLotLocalDataSource: ParkingLotRemoteDataSource
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

    override fun getAroundLotList(latitude: String, longitude: String): Single<List<LotEntity>> {
        return parkingLotLocalDataSource.getAroundLotList(
            AroundLotRequest(latitude, longitude)
        ).map { lotList ->
            lotList.map { lot ->
                lot.mapToDomainModel()
            }
        }
    }

    override fun getFilteredLotList(locationFilterOptionEntity: LocationFilterOptionEntity): Single<List<LotEntity>> {
        return parkingLotLocalDataSource.getFilteredLotList(
            locationFilterOptionEntity.mapFromDomainModel()
        ).map { lotList ->
            lotList.map { lot ->
                lot.mapToDomainModel()
            }
        }
    }
}