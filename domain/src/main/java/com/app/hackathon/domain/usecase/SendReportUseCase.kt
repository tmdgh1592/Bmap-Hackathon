package com.app.hackathon.domain.usecase

import com.app.hackathon.domain.entity.ReportEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.domain.repository.ReportRepository
import com.app.hackathon.domain.repository.SearchHistoryRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SendReportUseCase @Inject constructor(private val sendReportRepository: ReportRepository) {

    operator fun invoke(reportEntity: ReportEntity) {
        sendReportRepository.sendReport(reportEntity)
    }
}