package com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases

import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoneyBoxUseCase @Inject constructor(
    private val moneyBoxRepository: MoneyBoxRepository
) {
    operator fun invoke(): Flow<MoneyBox?> {
        return moneyBoxRepository.getMoneyBox()
    }
}
