package com.app.hackathon.data.repository

import com.app.hackathon.data.mapper.ReportMapper.mapFromDomainModel
import com.app.hackathon.data.source.remote.report.ReportRemoteDataSource
import com.app.hackathon.domain.entity.ReportEntity
import com.app.hackathon.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource
) : ReportRepository {

    override fun sendReport(reportEntity: ReportEntity) {
        reportRemoteDataSource.sendReport(reportEntity.mapFromDomainModel())
    }
}
