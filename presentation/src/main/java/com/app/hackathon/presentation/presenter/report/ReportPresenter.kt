package com.app.hackathon.presentation.presenter.report

import android.util.Log
import com.app.hackathon.domain.entity.ReportEntity
import com.app.hackathon.domain.usecase.*
import com.app.hackathon.presentation.view.report.ReportType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ReportPresenter @Inject constructor(
    private val sendReportUseCase: SendReportUseCase
) : ReportContract.Presenter<ReportContract.View> {

    private var view: ReportContract.View? = null
    private val disposables = CompositeDisposable()
    private var reportType: ReportType = ReportType.CAR


    override fun onAttach(view: ReportContract.View) {
        this.view = view
    }

    override fun onDetach() {
        view = null
        disposables.clear()
    }

    override fun requestSendReport(reportContent: String) {
        val report = ReportEntity(
            reportContent = reportContent
        )

        disposables.add(
            Completable.fromAction {
                sendReportUseCase(report)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d("TAG", "onSuccess Report")
                        view?.showAnimationAndFinish()
                    }, {
                        Log.d("TAG", "requestSendReport: ${it.message}")
                        view?.showErrorMessage()
                    }
                )
        )
    }

    override fun changeReportType(reportType: ReportType) {
        this.reportType = reportType
        view?.changeReportType(reportType)
    }


}