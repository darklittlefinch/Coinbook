package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp

class GetIncomeListFromDateUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getIncomeListFromDate(date: Timestamp): LiveData<List<Income>> {
        return operationsRepository.getIncomeListFromDate(date)
    }
}
