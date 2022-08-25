package com.app.hackathon.domain.entity

import java.io.Serializable


data class LotEntity(
    // 주차장명
    val parkName: String,
    // 도로명 주소
    val newAddr: String,
    // 위도
    val latitude: String,
    // 경도
    val longitude: String
): Serializable