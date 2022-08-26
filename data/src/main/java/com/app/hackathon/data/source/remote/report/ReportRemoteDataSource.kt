package com.app.hackathon.data.source.remote.report

import com.app.hackathon.data.model.ReportRequest

interface ReportRemoteDataSource {
    fun sendReport(report: ReportRequest)
}