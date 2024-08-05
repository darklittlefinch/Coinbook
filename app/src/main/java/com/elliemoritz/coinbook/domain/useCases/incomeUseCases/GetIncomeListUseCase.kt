package com.elliemoritz.coinbook.domain.useCases.incomeUseCases

import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.repositories.IncomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIncomeListUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    operator fun invoke(): Flow<List<Income>> {
        return repository.getIncomeList()
    }
}
