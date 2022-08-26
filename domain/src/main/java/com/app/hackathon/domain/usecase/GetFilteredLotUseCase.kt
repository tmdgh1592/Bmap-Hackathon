package com.app.hackathon.domain.usecase

import com.app.hackathon.domain.entity.LocationFilterOptionEntity
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.repository.ParkingLotRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetFilteredLotUseCase @Inject constructor(private val parkingLotRepository: ParkingLotRepository) {

    operator fun invoke(
        entity: LocationFilterOptionEntity
    ): Single<List<LotEntity>> =
        parkingLotRepository.getFilteredLotList(entity)

}