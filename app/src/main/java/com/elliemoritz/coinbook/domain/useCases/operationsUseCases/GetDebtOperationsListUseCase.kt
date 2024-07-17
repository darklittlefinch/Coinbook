package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class GetDebtOperationsListUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getDebtOperationsList(): LiveData<List<DebtOperation>> {
        return operationsRepository.getDebtOperationsList()
    }
}
