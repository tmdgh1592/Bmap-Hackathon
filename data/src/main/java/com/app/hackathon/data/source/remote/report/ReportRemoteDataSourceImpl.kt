package com.app.hackathon.data.source.remote.report

import com.app.hackathon.data.api.LotApiService
import com.app.hackathon.data.api.ReportApiService
import com.app.hackathon.data.model.AroundLotRequest
import com.app.hackathon.data.model.LotRequest
import com.app.hackathon.data.model.LotResponse
import com.app.hackathon.data.model.ReportRequest
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportApiService: ReportApiService
) : ReportRemoteDataSource {
    override fun sendReport(report: ReportRequest) {
        reportApiService.loadLotQuery(report)
    }
}