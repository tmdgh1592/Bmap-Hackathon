package com.app.hackathon.data.mapper

import com.app.hackathon.data.model.LotResponse
import com.app.hackathon.domain.entity.LotEntity

object LotMapper : Mapper<LotResponse, LotEntity> {

    override fun LotResponse.mapToDomainModel(): LotEntity {
        return LotEntity(parkCode, parkName, newAddr, latitude, longitude)
    }

    override fun LotEntity.mapFromDomainModel(): LotResponse {
        return LotResponse(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    }
}