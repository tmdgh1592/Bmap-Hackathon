package com.app.hackathon.data.source.local.searchhistory

import com.app.hackathon.data.db.dao.SearchHistoryDao
import com.app.hackathon.data.db.entity.SearchHistory
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchHistoryLocalDataSourceImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryLocalDataSource {

    override fun getSearchHistoryList(): Single<List<SearchHistory>> {
        return searchHistoryDao.getAll()
    }

    override fun addSearchHistory(history: SearchHistory) {
        searchHistoryDao.insertItem(history)
    }

    override fun removeSearchHistory(history: SearchHistory) {
        searchHistoryDao.deleteItem(history)

    }
}