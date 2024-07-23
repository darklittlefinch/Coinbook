package com.elliemoritz.coinbook.domain.useCases.operationsUseCases

import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import javax.inject.Inject

class RefreshOperationsDataUseCase @Inject constructor(
    private val operationsRepository: OperationsRepository
) {
    suspend operator fun invoke() {
        operationsRepository.refreshOperationData()
    }
}
