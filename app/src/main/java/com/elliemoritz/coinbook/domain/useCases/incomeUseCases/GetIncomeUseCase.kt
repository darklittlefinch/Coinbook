package com.elliemoritz.coinbook.domain.useCases.incomeUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.repositories.IncomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIncomeUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    operator fun invoke(id: Long): Flow<Income> {
        return repository.getIncome(id)
    }
}
