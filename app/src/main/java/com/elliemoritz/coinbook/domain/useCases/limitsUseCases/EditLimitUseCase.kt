package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import javax.inject.Inject

class EditLimitUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    suspend fun editLimit(limit: Limit) {
        limitsRepository.editLimit(limit)
    }
}
