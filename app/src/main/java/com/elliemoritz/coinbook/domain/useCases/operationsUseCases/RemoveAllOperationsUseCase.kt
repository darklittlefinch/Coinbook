package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import javax.inject.Inject

class RemoveAllOperationsUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend fun removeAllOperations() {
        operationsRepository.removeAllOperations()
    }
}
