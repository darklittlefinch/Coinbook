package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp
import javax.inject.Inject

class GetTotalIncomeAmountFromDateUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend fun getTotalIncomeAmountFromDate(date: Timestamp): Int {
        return operationsRepository.getTotalIncomeAmountFromDate(date)
    }
}
