package com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxOperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoneyBoxOperationsListUseCase @Inject constructor(
    private val repository: MoneyBoxOperationsRepository
) {
    operator fun invoke(): Flow<List<MoneyBoxOperation>> {
        return repository.getOperationsList()
    }
}
