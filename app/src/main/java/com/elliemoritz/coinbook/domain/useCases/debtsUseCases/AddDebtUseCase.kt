package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository

class AddDebtUseCase(
    private val debtsRepository: DebtsRepository
) {
    suspend fun addDebt(debt: Debt) {
        debtsRepository.addDebt(debt)
    }
}
