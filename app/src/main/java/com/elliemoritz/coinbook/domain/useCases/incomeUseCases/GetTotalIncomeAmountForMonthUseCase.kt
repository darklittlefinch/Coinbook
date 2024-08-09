package com.elliemoritz.coinbook.domain.useCases.incomeUseCases

import com.elliemoritz.coinbook.domain.repositories.IncomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalIncomeAmountForMonthUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getTotalIncomeAmountForMonth()
    }
}
