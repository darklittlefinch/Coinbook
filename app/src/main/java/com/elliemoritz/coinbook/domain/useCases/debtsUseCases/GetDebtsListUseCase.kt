package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDebtsListUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    operator fun invoke(): Flow<List<Debt>> {
        return debtsRepository.getDebtsList()
    }
}
