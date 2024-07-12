package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository

class GetDebtUseCase(
    private val debtsRepository: DebtsRepository
) {
    fun getDebt(id: Int): Debt {
        return debtsRepository.getDebt(id)
    }
}
