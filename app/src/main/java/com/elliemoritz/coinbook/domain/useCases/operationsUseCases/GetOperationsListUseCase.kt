package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class GetOperationsListUseCase(
    private val operationsRepository: OperationsRepository
) {
    fun getOperationsList(): LiveData<List<Operation>> {
        return operationsRepository.getOperationsList()
    }
}
