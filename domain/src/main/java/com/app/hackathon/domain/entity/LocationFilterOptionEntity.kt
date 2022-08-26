package com.app.hackathon.domain.entity

data class LocationFilterOptionEntity(
    val elevator: Boolean = false,
    val wideExit: Boolean = false,
    val ramp: Boolean = true,
    val accessRoads: Boolean = true,
    val wheelchairLift: Boolean = true,
    val brailleBlock: Boolean = true,
    val exGuidance: Boolean = true,
    val exTicketOffice: Boolean = true,
    val exRestroom: Boolean = true,
    val latitude: String = "35.1235125",
    val longitude: String = "123.1242421",
)