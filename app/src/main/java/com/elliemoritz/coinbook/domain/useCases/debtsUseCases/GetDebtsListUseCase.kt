package com.elliemoritz.coinbook.domain.useCases.debtsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import javax.inject.Inject

class GetDebtsListUseCase @Inject constructor(
    private val debtsRepository: DebtsRepository
) {
    fun getDebtsList(): LiveData<List<Debt>> {
        return debtsRepository.getDebtsList()
    }
}
