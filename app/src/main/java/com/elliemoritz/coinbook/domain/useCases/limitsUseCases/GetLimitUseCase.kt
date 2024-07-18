package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import javax.inject.Inject

class GetLimitUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    suspend operator fun invoke(id: Int): Limit {
        return limitsRepository.getLimit(id)
    }
}
