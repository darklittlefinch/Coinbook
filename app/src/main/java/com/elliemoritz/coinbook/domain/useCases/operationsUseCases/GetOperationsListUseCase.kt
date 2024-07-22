package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOperationsListUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(): Flow<List<Operation>> {
        return operationsRepository.getOperationsList()
    }
}
