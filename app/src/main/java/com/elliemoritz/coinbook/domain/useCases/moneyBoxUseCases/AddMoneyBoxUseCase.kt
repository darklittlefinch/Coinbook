package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository

class AddMoneyBoxUseCase(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    fun addMoneyBox(debt: MoneyBox) {
        moneyBoxRepository.addMoneyBox(debt)
    }
}
