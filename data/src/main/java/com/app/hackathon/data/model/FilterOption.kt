package com.app.hackathon.data.model

import com.google.gson.annotations.SerializedName

data class LocationFilterOptionRequest(
    @SerializedName("elevator")
    val elevator: Boolean = false,
    @SerializedName("wideExit")
    val wideExit: Boolean = false,
    @SerializedName("ramp")
    val ramp: Boolean = true,
    @SerializedName("accessRoads")
    val accessRoads: Boolean = true,
    @SerializedName("wheelchairLift")
    val wheelchairLift: Boolean = true,
    @SerializedName("brailleBlock")
    val brailleBlock: Boolean = true,
    @SerializedName("exGuidance")
    val exGuidance: Boolean = true,
    @SerializedName("exTicketOffice")
    val exTicketOffice: Boolean = true,
    @SerializedName("exRestroom")
    val exRestroom: Boolean = true,
    @SerializedName("longitude")
    val longitude: String = "123.1242421",
    @SerializedName("latitude")
    val latitude: String = "35.1235125"
)