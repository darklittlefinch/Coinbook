package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository

class EditDebtUseCase(
    private val debtsRepository: DebtsRepository
) {
    fun editDebt(debt: Debt) {
        debtsRepository.editDebt(debt)
    }
}
