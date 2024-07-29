package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import javax.inject.Inject

class AddToMoneyBoxUseCase @Inject constructor(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    suspend operator fun invoke(amount: Int) {
        moneyBoxRepository.addToMoneyBox(amount)
    }
}
