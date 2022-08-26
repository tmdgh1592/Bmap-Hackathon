package com.app.hackathon.data.model

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("prkplceNo")
    val prkplceNo: String = "111-10010-101",
    @SerializedName("reportType")
    val reportType: Int = 1,
    @SerializedName("reportTitle")
    val reportTitle: String = "",
    @SerializedName("reportText")
    val reportContent: String,
    @SerializedName("reportCarNm")
    val reportCarNm: String = "12가1234"
)