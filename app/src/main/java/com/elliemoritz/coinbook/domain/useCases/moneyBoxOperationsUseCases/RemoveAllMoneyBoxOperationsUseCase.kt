package com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases

import com.elliemoritz.coinbook.domain.repositories.MoneyBoxOperationsRepository
import javax.inject.Inject

class RemoveAllMoneyBoxOperationsUseCase @Inject constructor(
    private val repository: MoneyBoxOperationsRepository
) {
    suspend operator fun invoke() {
        repository.removeAllOperations()
    }
}
