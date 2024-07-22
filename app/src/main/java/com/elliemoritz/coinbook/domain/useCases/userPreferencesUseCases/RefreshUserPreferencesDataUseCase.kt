package com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases

import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository
import javax.inject.Inject

class RefreshUserPreferencesDataUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() {
        return userPreferencesRepository.refreshUserPreferencesData()
    }
}
