package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository

class AddLimitUseCase(
    private val limitsRepository: LimitsRepository
) {
    suspend fun addLimit(limit: Limit) {
        limitsRepository.addLimit(limit)
    }
}
