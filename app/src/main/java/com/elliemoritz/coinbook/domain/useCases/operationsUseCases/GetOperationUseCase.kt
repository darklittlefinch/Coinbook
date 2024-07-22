package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOperationUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(id: Int): Flow<Operation> {
        return operationsRepository.getOperation(id)
    }
}
