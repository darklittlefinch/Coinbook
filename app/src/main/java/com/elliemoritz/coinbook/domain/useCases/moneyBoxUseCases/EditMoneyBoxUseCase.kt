package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository

class EditMoneyBoxUseCase(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    suspend fun editMoneyBox(moneyBox: MoneyBox) {
        moneyBoxRepository.editMoneyBox(moneyBox)
    }
}
