package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import javax.inject.Inject

class GetDebtOperationsListUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    operator fun invoke(): LiveData<List<DebtOperation>> {
        return operationsRepository.getDebtOperationsList()
    }
}
