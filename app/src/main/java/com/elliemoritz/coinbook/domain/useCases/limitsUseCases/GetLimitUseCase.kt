package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLimitUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    operator fun invoke(id: Int): Flow<Limit> {
        return limitsRepository.getLimit(id)
    }
}
