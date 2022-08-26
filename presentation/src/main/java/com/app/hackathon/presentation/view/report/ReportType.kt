package com.app.hackathon.presentation.view.report

enum class ReportType {
    ERROR, CAR;

    fun changeMode(): ReportType {
        return when(this) {
            ERROR -> CAR
            CAR -> ERROR
            else -> CAR
        }
    }
}