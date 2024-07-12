package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class RemoveOperationUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun removeOperation(operation: Operation) {
        operationsRepository.removeOperation(operation)
    }
}
