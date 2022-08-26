package com.app.hackathon.domain.entity


data class ReportEntity(
    val prkplceNo: String = "111-10010-101",
    val reportType: Int = 1,
    val reportTitle: String = "",
    val reportContent: String,
    val reportCarNm: String = "12가1234"
)