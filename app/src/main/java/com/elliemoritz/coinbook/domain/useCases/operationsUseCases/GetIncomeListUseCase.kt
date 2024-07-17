package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class GetIncomeListUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getIncomeList(): LiveData<List<Income>> {
        return operationsRepository.getIncomeList()
    }
}
