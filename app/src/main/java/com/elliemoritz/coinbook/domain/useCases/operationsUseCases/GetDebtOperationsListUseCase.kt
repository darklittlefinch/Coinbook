package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDebtOperationsListUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(): Flow<List<DebtOperation>> {
        return operationsRepository.getDebtOperationsList()
    }
}
