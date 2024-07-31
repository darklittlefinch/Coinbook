package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesListForMonthUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(): Flow<List<Expense>> {
        return operationsRepository.getExpensesListForMonth()
    }
}
