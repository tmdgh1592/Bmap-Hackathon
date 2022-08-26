package com.app.hackathon.data.api

import com.app.hackathon.data.model.AroundLotRequest
import com.app.hackathon.data.model.LocationFilterOptionRequest
import com.app.hackathon.data.model.LotRequest
import com.app.hackathon.data.model.LotResponse
import com.app.hackathon.domain.entity.LocationFilterOptionEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface LotApiService {
    @POST("park/find/address")
    //@HTTP(method = "GET", path = "park/find/address", hasBody = true)
    fun loadLotQuery(
//        @Query("address") address: String,
//        @Query("latitude") latitude: String,
//        @Query("longitude") longitude: String
        @Body lotRequest: LotRequest
    ): Single<List<LotResponse>>

    @POST("park/find/location")
    //@HTTP(method = "GET", path = "park/find/location", hasBody = true)
    fun loadAroundLot(
//        @Query("latitude") latitude: String,
//        @Query("longitude") longitude: String
        @Body aroundLotRequest: AroundLotRequest
    ): Single<List<LotResponse>>

    @POST("park/find/amenity")
    fun loadFilteredLot(
        @Body locationFilterOptionRequest: LocationFilterOptionRequest
    ): Single<List<LotResponse>>

}