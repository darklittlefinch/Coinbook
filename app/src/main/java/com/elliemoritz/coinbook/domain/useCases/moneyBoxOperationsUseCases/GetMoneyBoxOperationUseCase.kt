package com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxOperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoneyBoxOperationUseCase @Inject constructor(
    private val repository: MoneyBoxOperationsRepository
) {
    operator fun invoke(id: Long): Flow<MoneyBoxOperation> {
        return repository.getOperation(id)
    }
}
