package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository

class RemoveMoneyBoxUseCase(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    suspend fun removeMoneyBox(moneyBox: MoneyBox) {
        moneyBoxRepository.removeMoneyBox(moneyBox)
    }
}
