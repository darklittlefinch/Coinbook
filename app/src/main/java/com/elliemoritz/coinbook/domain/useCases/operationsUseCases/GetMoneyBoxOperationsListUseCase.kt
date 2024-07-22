package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp
import javax.inject.Inject

class GetMoneyBoxOperationsListUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(date: Timestamp): Flow<List<MoneyBoxOperation>> {
        return operationsRepository.getMoneyBoxOperationsListFromDate(date)
    }
}
