package com.elliemoritz.coinbook.domain.useCases.expensesUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryExpensesListForMonthUseCase @Inject constructor(
    private val repository: ExpensesRepository
) {
    operator fun invoke(categoryId: Int): Flow<List<Expense>> {
        return repository.getCategoryExpensesListForMonth(categoryId)
    }
}
