package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import javax.inject.Inject

class AddAlarmUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    suspend operator fun invoke(alarm: Alarm) {
        alarmsRepository.addAlarm(alarm)
    }
}
