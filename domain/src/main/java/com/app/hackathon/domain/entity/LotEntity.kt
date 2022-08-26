package com.app.hackathon.domain.entity

import java.io.Serializable


data class LotEntity(
    // 주차장 코드
    val parkCode: String,
    // 주차장명
    val parkName: String,
    // 도로명 주소
    val newAddr: String,
    // 위도
    val latitude: String,
    // 경도
    val longitude: String
): Serializable {
    val parkState: String = when (parkName.length) {
        in 0..6 -> "여유"
        in 7..11 -> "혼잡"
        else -> "만차"
    }
    val nowParkCount = when (parkName.length) {
        in 0..6 -> 0
        in 7..11 -> 3
        else -> 10
    }
    val maxParkCount = when (parkName.length) {
        in 0..6 -> 3
        in 7..11 -> 5
        else -> 10
    }
}