package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class AddOperationUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun addOperation(operation: Operation) {
        operationsRepository.addOperation(operation)
    }
}
