package com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases

import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.getNotificationsEnabled()
    }
}
