package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import javax.inject.Inject

class RemoveAlarmUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    suspend fun removeAlarm(alarm: Alarm) {
        alarmsRepository.removeAlarm(alarm)
    }
}
