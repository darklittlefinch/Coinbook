package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository

class EditLimitUseCase(
    private val limitsRepository: LimitsRepository
) {
    fun editLimit(limit: Limit) {
        limitsRepository.editLimit(limit)
    }
}
