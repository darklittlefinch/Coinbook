package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalIncomeAmountForMonthUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(): Flow<Int> {
        return operationsRepository.getTotalIncomeAmountForMonth()
    }
}
