package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLimitsListUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    operator fun invoke(): Flow<List<Limit>> {
        return limitsRepository.getLimitsList()
    }
}
