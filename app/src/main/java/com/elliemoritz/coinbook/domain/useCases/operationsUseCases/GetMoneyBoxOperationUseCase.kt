package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoneyBoxOperationUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(id: Int): Flow<MoneyBoxOperation> {
        return operationsRepository.getMoneyBoxOperation(id)
    }
}
