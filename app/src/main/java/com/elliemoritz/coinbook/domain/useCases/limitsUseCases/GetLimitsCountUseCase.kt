package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLimitsCountUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    operator fun invoke(): Flow<Long> {
        return limitsRepository.getLimitsCount()
    }
}