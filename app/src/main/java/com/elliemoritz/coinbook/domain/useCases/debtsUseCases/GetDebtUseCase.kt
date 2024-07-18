package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import javax.inject.Inject

class GetDebtUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    suspend operator fun invoke(id: Int): Debt {
        return debtsRepository.getDebt(id)
    }
}
