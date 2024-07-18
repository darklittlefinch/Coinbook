package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp
import javax.inject.Inject

class GetMoneyBoxOperationsListUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    fun getMoneyBoxOperationsList(date: Timestamp): LiveData<List<MoneyBoxOperation>> {
        return operationsRepository.getMoneyBoxOperationsListFromDate(date)
    }
}
