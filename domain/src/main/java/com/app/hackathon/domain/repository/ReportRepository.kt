package com.app.hackathon.domain.repository

import com.app.hackathon.domain.entity.ReportEntity

interface ReportRepository {
    fun sendReport(reportEntity: ReportEntity)
}