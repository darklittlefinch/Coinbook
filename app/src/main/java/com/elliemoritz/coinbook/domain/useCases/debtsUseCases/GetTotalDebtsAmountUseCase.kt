package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import javax.inject.Inject

class GetTotalDebtsAmountUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    suspend fun getTotalDebtsAmount(): Int {
        return debtsRepository.getTotalDebtsAmount()
    }
}
