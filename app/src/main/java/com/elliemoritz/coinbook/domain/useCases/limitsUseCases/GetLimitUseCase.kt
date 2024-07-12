package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository

class GetLimitUseCase(
    private val limitsRepository: LimitsRepository
) {
    fun getLimit(id: Int): Limit {
        return limitsRepository.getLimit(id)
    }
}
