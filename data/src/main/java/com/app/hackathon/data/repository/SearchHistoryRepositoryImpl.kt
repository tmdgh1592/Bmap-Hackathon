package com.app.hackathon.data.repository

import com.app.hackathon.data.mapper.SearchHistoryMapper.mapFromDomainModel
import com.app.hackathon.data.mapper.SearchHistoryMapper.mapToDomainModel
import com.app.hackathon.data.source.local.searchhistory.SearchHistoryLocalDataSource
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.domain.repository.SearchHistoryRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryLocalDataSource: SearchHistoryLocalDataSource
) : SearchHistoryRepository {

    override fun getSearchHistoryList(): Single<List<SearchHistoryEntity>> {
        return searchHistoryLocalDataSource.getSearchHistoryList().map { searchHistoryList ->
            searchHistoryList.map { searchHistory ->
                searchHistory.mapToDomainModel()
            }
        }
    }

    override fun addSearchHistory(searchHistory: SearchHistoryEntity) {
        searchHistoryLocalDataSource.addSearchHistory(
            searchHistory.mapFromDomainModel()
        )
    }

    override fun removeSearchHistory(searchHistory: SearchHistoryEntity) {
        searchHistoryLocalDataSource.removeSearchHistory(
            searchHistory.mapFromDomainModel()
        )
    }
}