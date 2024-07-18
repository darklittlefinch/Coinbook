package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import javax.inject.Inject

class EditDebtUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    suspend fun editDebt(debt: Debt) {
        debtsRepository.editDebt(debt)
    }
}
