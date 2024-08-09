package com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.DebtsOperationsRepository
import javax.inject.Inject

class AddDebtOperationUseCase @Inject constructor(
    private val repository: DebtsOperationsRepository
) {
    suspend operator fun invoke(operation: DebtOperation) {
        repository.addOperation(operation)
    }
}
