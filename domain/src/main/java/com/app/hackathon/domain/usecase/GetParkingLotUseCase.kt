package com.app.hackathon.domain.usecase

import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.repository.ParkingLotRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetParkingLotUseCase @Inject constructor(private val parkingLotRepository: ParkingLotRepository) {

    operator fun invoke(
        query: String,
        latitude: String,
        longitude: String
    ): Single<List<LotEntity>> =
        parkingLotRepository.getLotList(query, latitude, longitude)

}