package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import javax.inject.Inject

class EditOperationUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend fun editOperation(operation: Operation) {
        operationsRepository.editOperation(operation)
    }
}
