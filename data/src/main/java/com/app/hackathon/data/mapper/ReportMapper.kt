package com.app.hackathon.data.mapper

import com.app.hackathon.data.model.ReportRequest
import com.app.hackathon.domain.entity.ReportEntity

object ReportMapper : Mapper<ReportRequest, ReportEntity> {

    override fun ReportRequest.mapToDomainModel(): ReportEntity {
        return ReportEntity(prkplceNo, reportType, reportTitle, reportContent, reportCarNm)
    }

    override fun ReportEntity.mapFromDomainModel(): ReportRequest {
        return ReportRequest(prkplceNo, reportType, reportTitle, reportContent, reportCarNm)
    }
}