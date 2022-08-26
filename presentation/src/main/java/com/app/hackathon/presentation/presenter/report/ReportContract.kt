package com.app.hackathon.presentation.presenter.report

import com.app.hackathon.presentation.base.BaseContract
import com.app.hackathon.presentation.view.report.ReportType

interface ReportContract {
    interface View : BaseContract.BaseView {
        fun justFinish()
        fun showAnimationAndFinish()
        fun showErrorMessage()
        fun changeReportType(reportType: ReportType)
    }

    interface Presenter<T : BaseContract.BaseView> : BaseContract.BasePresenter<T> {
        fun requestSendReport(reportContent: String)
        fun changeReportType(reportType: ReportType)
    }
}