package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalDebtsAmountUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    operator fun invoke(): Flow<Int> {
        return debtsRepository.getTotalDebtsAmount()
    }
}
