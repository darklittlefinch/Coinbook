package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp

class GetCategoryExpensesListUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getOperationsListFromDate(categoryName: String, date: Timestamp): LiveData<List<Expense>> {
        return operationsRepository.getCategoryExpensesListFromDate(categoryName, date)
    }
}
