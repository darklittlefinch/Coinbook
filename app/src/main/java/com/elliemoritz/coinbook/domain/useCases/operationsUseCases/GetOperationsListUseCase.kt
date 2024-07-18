package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import javax.inject.Inject

class GetOperationsListUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    fun getOperationsList(): LiveData<List<Operation>> {
        return operationsRepository.getOperationsList()
    }
}
