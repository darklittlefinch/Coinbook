package com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.DebtsOperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDebtOperationByDebtIdUseCase @Inject constructor(
    private val repository: DebtsOperationsRepository
) {
    operator fun invoke(debtId: Long): Flow<DebtOperation> {
        return repository.getOperationByDebtId(debtId)
    }
}
