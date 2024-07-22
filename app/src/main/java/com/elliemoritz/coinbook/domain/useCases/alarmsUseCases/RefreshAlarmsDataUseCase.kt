package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import javax.inject.Inject

class RefreshAlarmsDataUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    suspend operator fun invoke() {
        alarmsRepository.refreshAlarmsData()
    }
}
