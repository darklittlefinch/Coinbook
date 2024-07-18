package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp
import javax.inject.Inject

class GetTotalExpensesAmountFromDateUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend operator fun invoke(date: Timestamp): Int {
        return operationsRepository.getTotalExpensesAmountFromDate(date)
    }
}
