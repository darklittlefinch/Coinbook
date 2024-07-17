package com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases

import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository

class AddToBalanceUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend fun addToBalance(amount: Int) {
        return userPreferencesRepository.addToBalance(amount)
    }
}
