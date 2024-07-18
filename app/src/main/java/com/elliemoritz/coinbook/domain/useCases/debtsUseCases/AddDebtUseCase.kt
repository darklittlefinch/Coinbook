package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import javax.inject.Inject

class AddDebtUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    suspend operator fun invoke(debt: Debt) {
        debtsRepository.addDebt(debt)
    }
}
