package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import javax.inject.Inject

class GetLimitsCountUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    suspend operator fun invoke(): Int {
        return limitsRepository.getLimitsCount()
    }
}