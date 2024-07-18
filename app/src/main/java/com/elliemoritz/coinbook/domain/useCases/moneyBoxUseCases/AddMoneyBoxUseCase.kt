package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import javax.inject.Inject

class AddMoneyBoxUseCase @Inject constructor(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    suspend fun addMoneyBox(debt: MoneyBox) {
        moneyBoxRepository.addMoneyBox(debt)
    }
}
