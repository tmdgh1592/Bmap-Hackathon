package com.app.hackathon.data.api

import com.app.hackathon.data.response.LotRequest
import com.app.hackathon.data.response.LotResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface LotApiService {
    @POST("park/find/address/")
    fun loadLotQuery(
        @Body lotRequest: LotRequest
    ): Single<List<LotResponse>>
}