package com.elliemoritz.coinbook.domain.useCases.expensesUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.ExpensesRepository
import javax.inject.Inject

class EditExpenseUseCase @Inject constructor(
    private val repository: ExpensesRepository
) {
    suspend operator fun invoke(expense: Expense) {
        repository.editExpense(expense)
    }
}
