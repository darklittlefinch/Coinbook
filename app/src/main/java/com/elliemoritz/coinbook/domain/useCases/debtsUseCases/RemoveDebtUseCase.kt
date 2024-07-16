package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository

class RemoveDebtUseCase(
    private val debtsRepository: DebtsRepository
) {
    suspend fun removeDebt(debt: Debt) {
        debtsRepository.removeDebt(debt)
    }
}
