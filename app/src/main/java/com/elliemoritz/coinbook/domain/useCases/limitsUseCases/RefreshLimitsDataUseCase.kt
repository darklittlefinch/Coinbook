package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import javax.inject.Inject

class RefreshLimitsDataUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    suspend operator fun invoke() {
        return limitsRepository.refreshLimitsData()
    }
}