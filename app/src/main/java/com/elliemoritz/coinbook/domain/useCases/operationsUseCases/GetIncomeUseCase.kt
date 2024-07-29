package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIncomeUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(id: Int): Flow<Income> {
        return operationsRepository.getIncome(id)
    }
}
