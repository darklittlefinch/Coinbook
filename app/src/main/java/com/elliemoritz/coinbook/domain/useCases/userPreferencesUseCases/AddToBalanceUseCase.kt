package com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases

import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository
import javax.inject.Inject

class AddToBalanceUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend fun addToBalance(amount: Int) {
        return userPreferencesRepository.addToBalance(amount)
    }
}
