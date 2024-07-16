package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository

class RemoveLimitUseCase(
    private val limitsRepository: LimitsRepository
) {
    suspend fun removeLimit(limit: Limit) {
        limitsRepository.removeLimit(limit)
    }
}
