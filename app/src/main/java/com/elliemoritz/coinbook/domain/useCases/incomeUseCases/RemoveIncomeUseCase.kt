package com.elliemoritz.coinbook.domain.useCases.incomeUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.repositories.IncomeRepository
import javax.inject.Inject

class RemoveIncomeUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke(income: Income) {
        repository.removeIncome(income)
    }
}
