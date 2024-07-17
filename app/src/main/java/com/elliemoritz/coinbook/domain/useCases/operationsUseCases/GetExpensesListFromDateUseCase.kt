package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp

class GetExpensesListFromDateUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getOperationsListFromDate(date: Timestamp): LiveData<List<Expense>> {
        return operationsRepository.getExpensesListFromDate(date)
    }
}
