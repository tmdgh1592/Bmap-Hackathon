package com.app.hackathon.domain.usecase

import com.app.hackathon.domain.model.SearchHistory
import com.app.hackathon.domain.repository.SearchHistoryRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(private val searchHistoryRepository: SearchHistoryRepository) {
    operator fun invoke(): Single<List<SearchHistory>> {
        return searchHistoryRepository.getSearchHistoryList()
    }
}