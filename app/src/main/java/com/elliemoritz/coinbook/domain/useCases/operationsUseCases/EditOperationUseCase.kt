package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class EditOperationUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun editOperation(operation: Operation) {
        operationsRepository.editOperation(operation)
    }
}
