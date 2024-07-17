package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class GetExpensesListUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getOperationsList(): LiveData<List<Expense>> {
        return operationsRepository.getExpensesList()
    }
}
