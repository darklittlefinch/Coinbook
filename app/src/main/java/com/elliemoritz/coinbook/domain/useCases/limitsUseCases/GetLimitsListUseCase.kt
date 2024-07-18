package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import javax.inject.Inject

class GetLimitsListUseCase @Inject constructor(
    private val limitsRepository: LimitsRepository
) {
    operator fun invoke(): LiveData<List<Limit>> {
        return limitsRepository.getLimitsList()
    }
}
