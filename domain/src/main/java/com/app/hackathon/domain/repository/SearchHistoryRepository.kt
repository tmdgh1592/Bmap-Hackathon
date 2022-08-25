package com.app.hackathon.domain.repository

import com.app.hackathon.domain.entity.SearchHistoryEntity
import io.reactivex.rxjava3.core.Single

interface SearchHistoryRepository {
    fun getSearchHistoryList(): Single<List<SearchHistoryEntity>>
    fun addSearchHistory(searchHistory: SearchHistoryEntity)
    fun removeSearchHistory(searchHistory: SearchHistoryEntity)
}