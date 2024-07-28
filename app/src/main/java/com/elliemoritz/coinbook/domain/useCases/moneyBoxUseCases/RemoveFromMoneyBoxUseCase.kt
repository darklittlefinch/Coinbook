package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import javax.inject.Inject

class RemoveFromMoneyBoxUseCase @Inject constructor(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    suspend operator fun invoke(amount: Int) {
        moneyBoxRepository.removeFromMoneyBox(amount)
    }
}
