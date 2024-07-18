package com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases

import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository
import javax.inject.Inject

class RemoveFromBalanceUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend fun removeFromBalance(amount: Int) {
        return userPreferencesRepository.removeFromBalance(amount)
    }
}
