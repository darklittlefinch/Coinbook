package com.elliemoritz.coinbook.domain.useCases.limitsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository

class GetLimitsListUseCase(
    private val limitsRepository: LimitsRepository
) {
    fun getLimitsList(): LiveData<List<Limit>> {
        return limitsRepository.getLimitsList()
    }
}
