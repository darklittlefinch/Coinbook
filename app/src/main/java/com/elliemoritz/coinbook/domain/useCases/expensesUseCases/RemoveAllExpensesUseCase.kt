package com.elliemoritz.coinbook.domain.useCases.expensesUseCases

import com.elliemoritz.coinbook.domain.repositories.ExpensesRepository
import javax.inject.Inject

class RemoveAllExpensesUseCase @Inject constructor(
    private val repository: ExpensesRepository
) {
    suspend operator fun invoke() {
        repository.removeAllExpenses()
    }
}
