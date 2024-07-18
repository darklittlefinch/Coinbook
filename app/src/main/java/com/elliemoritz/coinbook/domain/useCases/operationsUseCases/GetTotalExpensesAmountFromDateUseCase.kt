package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp
import javax.inject.Inject

class GetTotalExpensesAmountFromDateUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend fun getTotalExpensesAmountFromDate(date: Timestamp): Int {
        return operationsRepository.getTotalExpensesAmountFromDate(date)
    }
}
