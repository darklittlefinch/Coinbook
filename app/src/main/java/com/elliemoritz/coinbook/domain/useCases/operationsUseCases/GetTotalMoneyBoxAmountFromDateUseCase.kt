package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp
import javax.inject.Inject

class GetTotalMoneyBoxAmountFromDateUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend fun getTotalMoneyBoxAmountFromDate(date: Timestamp): Int {
        return operationsRepository.getTotalMoneyBoxAmountFromDate(date)
    }
}
