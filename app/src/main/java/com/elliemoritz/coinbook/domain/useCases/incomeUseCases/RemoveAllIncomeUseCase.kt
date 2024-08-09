package com.elliemoritz.coinbook.domain.useCases.incomeUseCases

import com.elliemoritz.coinbook.domain.repositories.IncomeRepository
import javax.inject.Inject

class RemoveAllIncomeUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke() {
        repository.removeAllIncome()
    }
}
