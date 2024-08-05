package com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxOperationsRepository
import javax.inject.Inject

class EditMoneyBoxOperationUseCase @Inject constructor(
    private val repository: MoneyBoxOperationsRepository
) {
    suspend operator fun invoke(operation: MoneyBoxOperation) {
        repository.editOperation(operation)
    }
}
