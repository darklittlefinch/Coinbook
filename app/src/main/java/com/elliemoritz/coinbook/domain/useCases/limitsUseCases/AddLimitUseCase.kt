package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import javax.inject.Inject

class AddLimitUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    suspend operator fun invoke(limit: Limit) {
        limitsRepository.addLimit(limit)
    }
}
