package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryExpensesListForMonthUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(categoryName: String): Flow<List<Expense>> {
        return operationsRepository.getCategoryExpensesListForMonth(categoryName)
    }
}
