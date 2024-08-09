package com.elliemoritz.coinbook.domain.useCases.expensesUseCases

import com.elliemoritz.coinbook.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalExpensesAmountByCategoryForMonthUseCase @Inject constructor(
    private val repository: ExpensesRepository
) {
    operator fun invoke(categoryId: Long): Flow<Int> {
        return repository.getTotalExpensesAmountByCategoryForMonth(categoryId)
    }
}
