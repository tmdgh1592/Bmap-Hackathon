package com.app.hackathon.data.api

import com.app.hackathon.data.model.ReportRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApiService {
    @POST("report/add")
    fun loadLotQuery(
        @Body reportRequest: ReportRequest
    ): Call<Any>
}