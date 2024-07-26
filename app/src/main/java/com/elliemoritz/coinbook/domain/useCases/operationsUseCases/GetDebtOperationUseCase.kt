package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDebtOperationUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(id: Int): Flow<DebtOperation> {
        return operationsRepository.getDebtOperation(id)
    }
}
