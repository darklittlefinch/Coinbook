package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import javax.inject.Inject

class GetOperationUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend fun getOperation(id: Int): Operation {
        return operationsRepository.getOperation(id)
    }
}
