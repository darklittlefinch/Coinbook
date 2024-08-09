package com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.DebtsOperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDebtOperationsListUseCase @Inject constructor(
    private val repository: DebtsOperationsRepository
) {
    operator fun invoke(): Flow<List<DebtOperation>> {
        return repository.getOperationsList()
    }
}
