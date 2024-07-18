package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import javax.inject.Inject

class AddOperationUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend fun addOperation(operation: Operation) {
        operationsRepository.addOperation(operation)
    }
}
