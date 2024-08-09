package com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases

import com.elliemoritz.coinbook.domain.repositories.DebtsOperationsRepository
import javax.inject.Inject

class RemoveAllDebtOperationsUseCase @Inject constructor(
    private val repository: DebtsOperationsRepository
) {
    suspend operator fun invoke() {
        repository.removeAllOperations()
    }
}
