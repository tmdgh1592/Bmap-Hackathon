package com.app.hackathon.data.source.local.searchhistory

import com.app.hackathon.data.db.entity.SearchHistory
import io.reactivex.rxjava3.core.Single

interface SearchHistoryLocalDataSource {
    fun getSearchHistoryList(): Single<List<SearchHistory>>
    fun addSearchHistory(history: SearchHistory)
    fun removeSearchHistory(history: SearchHistory)
}