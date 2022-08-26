package com.app.hackathon.data.mapper

import com.app.hackathon.data.db.entity.SearchHistory
import com.app.hackathon.domain.entity.SearchHistoryEntity

object SearchHistoryMapper : Mapper<SearchHistory, SearchHistoryEntity> {

    override fun SearchHistoryEntity.mapFromDomainModel(): SearchHistory =
        SearchHistory(id, parkCode, lotName, newAddr, latitude, longitude)

    override fun SearchHistory.mapToDomainModel(): SearchHistoryEntity =
        SearchHistoryEntity(parkCode, lotName, newAddr, latitude, longitude)
}