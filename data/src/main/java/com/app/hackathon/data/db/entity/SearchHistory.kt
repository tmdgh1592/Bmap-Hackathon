package com.app.hackathon.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.hackathon.data.Constants.SEARCH_HISTORY_TABLE_NAME

@Entity(tableName = SEARCH_HISTORY_TABLE_NAME)
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val lotName: String,
    // 도로명 주소
    val newAddr: String,
    // 위도
    val latitude: String,
    // 경도
    val longitude: String
)
