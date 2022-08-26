package com.app.hackathon.data.mapper

import com.app.hackathon.data.model.LocationFilterOptionRequest
import com.app.hackathon.domain.entity.LocationFilterOptionEntity

object FilterOptionMapper : Mapper<LocationFilterOptionRequest, LocationFilterOptionEntity> {

    override fun LocationFilterOptionRequest.mapToDomainModel(): LocationFilterOptionEntity {
        return LocationFilterOptionEntity(elevator, wideExit, ramp, accessRoads, wheelchairLift, brailleBlock, exGuidance, exTicketOffice, exRestroom, longitude)
    }

    override fun LocationFilterOptionEntity.mapFromDomainModel(): LocationFilterOptionRequest {
        return LocationFilterOptionRequest(elevator, wideExit, ramp, accessRoads, wheelchairLift, brailleBlock, exGuidance, exTicketOffice, exRestroom, longitude)
    }
}