package com.app.hackathon.data.mapper

import com.app.hackathon.data.db.entity.SearchHistory
import com.app.hackathon.domain.entity.SearchHistoryEntity

object SearchHistoryMapper : Mapper<SearchHistory, SearchHistoryEntity> {

    override fun SearchHistoryEntity.mapFromDomainModel(): SearchHistory =
        SearchHistory(id, lotName)

    override fun SearchHistory.mapToDomainModel(): SearchHistoryEntity =
        SearchHistoryEntity(id, lotName)
}