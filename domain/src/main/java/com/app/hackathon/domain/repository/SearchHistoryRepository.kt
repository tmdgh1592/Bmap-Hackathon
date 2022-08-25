package com.app.hackathon.domain.repository

import com.app.hackathon.domain.model.SearchHistory
import io.reactivex.rxjava3.core.Single

interface SearchHistoryRepository {
    fun getSearchHistoryList(): Single<List<SearchHistory>>
}