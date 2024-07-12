package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class GetOperationUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getOperation(id: Int): Operation {
        return operationsRepository.getOperation(id)
    }
}
