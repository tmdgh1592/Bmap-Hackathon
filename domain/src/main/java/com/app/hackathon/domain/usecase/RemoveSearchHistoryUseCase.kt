package com.app.hackathon.domain.usecase

import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.domain.repository.SearchHistoryRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RemoveSearchHistoryUseCase @Inject constructor(private val searchHistoryRepository: SearchHistoryRepository) {

    operator fun invoke(searchHistoryEntity: SearchHistoryEntity) {
        searchHistoryRepository.removeSearchHistory(searchHistoryEntity)
    }
}