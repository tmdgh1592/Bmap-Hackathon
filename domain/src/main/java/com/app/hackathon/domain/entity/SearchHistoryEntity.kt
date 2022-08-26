package com.app.hackathon.domain.entity


data class SearchHistoryEntity(
    val parkCode: String,
    val lotName: String,
    // 도로명 주소
    val newAddr: String,
    // 위도
    val latitude: String,
    // 경도
    val longitude: String
) {
    val id: Long = 0
}
