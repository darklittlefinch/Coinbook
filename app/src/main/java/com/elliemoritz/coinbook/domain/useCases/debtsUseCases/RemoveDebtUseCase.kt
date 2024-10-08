package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import javax.inject.Inject

class RemoveDebtUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    suspend operator fun invoke(debt: Debt) {
        debtsRepository.removeDebt(debt)
    }
}
