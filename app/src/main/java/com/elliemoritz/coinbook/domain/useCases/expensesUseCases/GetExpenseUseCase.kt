package com.elliemoritz.coinbook.domain.useCases.expensesUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseUseCase @Inject constructor(
    private val repository: ExpensesRepository
) {
    operator fun invoke(id: Int): Flow<Expense> {
        return repository.getExpense(id)
    }
}
