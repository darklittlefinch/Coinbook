package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import javax.inject.Inject

class GetAlarmsCountUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    suspend operator fun invoke(): Int {
        return alarmsRepository.getAlarmsCount()
    }
}