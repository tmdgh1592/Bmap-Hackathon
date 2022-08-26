package com.app.hackathon.domain.usecase

import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.repository.ParkingLotRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAroundLotUseCase @Inject constructor(private val parkingLotRepository: ParkingLotRepository) {

    operator fun invoke(
        latitude: String,
        longitude: String
    ): Single<List<LotEntity>> =
        parkingLotRepository.getAroundLotList(latitude, longitude)

}