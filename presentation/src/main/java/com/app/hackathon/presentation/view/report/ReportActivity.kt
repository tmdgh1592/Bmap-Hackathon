package com.app.hackathon.presentation.view.report

import android.animation.Animator
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityReportBinding
import com.app.hackathon.presentation.presenter.report.ReportContract
import com.app.hackathon.presentation.presenter.report.ReportPresenter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportActivity(override val layoutResId: Int = R.layout.activity_report) :
    BaseActivity<ActivityReportBinding>(), ReportContract.View {

    @Inject
    lateinit var presenter: ReportPresenter

    override fun initActivity() {
        presenter.onAttach(this)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        with(binding) {
            doneBtn.setOnClickListener {
                val reportContent = binding.reportEditText.text.toString()
                presenter.requestSendReport(reportContent)
            }

            errorReportBtn.setOnClickListener {
                presenter.changeReportType(ReportType.ERROR)
            }

            carReportBtn.setOnClickListener {
                presenter.changeReportType(ReportType.CAR)
            }

            backBtn.setOnClickListener {
                justFinish()
            }
        }
    }

    override fun justFinish() {
        finish()
    }

    override fun showAnimationAndFinish() {
        with(binding.checkLottieView) {
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                    super.onAnimationStart(animation, isReverse)
                }

                override fun onAnimationStart(p0: Animator?) {
                    Log.d("TAG", "onAnimationStart: 시작")
                }

                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    super.onAnimationEnd(animation, isReverse)
                    justFinish()
                }

                override fun onAnimationEnd(p0: Animator?) {
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })

            playAnimation()
        }
    }

    override fun showErrorMessage() {
        showToast("에러가 발생했어요!\n잠시 후 다시 시도해주세요.")
    }

    override fun changeReportType(reportType: ReportType) {
        if (reportType == ReportType.ERROR) {
            convertErrorReportState()
        } else {
            convertCarReportState()
        }
    }

    private fun convertErrorReportState() {
        with(binding) {
            errorReportBtn.background = ContextCompat.getDrawable(this@ReportActivity, R.drawable.bg_checked_report_type)
            errorReportTv.setTextColor(ContextCompat.getColor(this@ReportActivity, R.color.blue))
            errorReportCheckIv.visibility = View.VISIBLE

            carReportBtn.background = ContextCompat.getDrawable(this@ReportActivity, R.drawable.bg_unchecked_report_type)
            carReportTv.setTextColor(ContextCompat.getColor(this@ReportActivity, R.color.black))
            carReportCheckIv.visibility = View.GONE
        }
    }

    private fun convertCarReportState() {
        with(binding) {
            carReportBtn.background = ContextCompat.getDrawable(this@ReportActivity, R.drawable.bg_checked_report_type)
            carReportTv.setTextColor(ContextCompat.getColor(this@ReportActivity, R.color.blue))
            carReportCheckIv.visibility = View.VISIBLE

            errorReportBtn.background = ContextCompat.getDrawable(this@ReportActivity, R.drawable.bg_unchecked_report_type)
            errorReportTv.setTextColor(ContextCompat.getColor(this@ReportActivity, R.color.black))
            errorReportCheckIv.visibility = View.GONE
        }
    }


}