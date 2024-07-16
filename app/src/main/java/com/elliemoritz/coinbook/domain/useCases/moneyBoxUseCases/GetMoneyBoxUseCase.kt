package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository

class GetMoneyBoxUseCase(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    suspend fun getMoneyBox(id: Int): MoneyBox {
        return moneyBoxRepository.getMoneyBox(id)
    }
}
