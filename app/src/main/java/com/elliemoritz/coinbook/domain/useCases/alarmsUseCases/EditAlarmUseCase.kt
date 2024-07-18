package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import javax.inject.Inject

class EditAlarmUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    suspend fun editAlarm(alarm: Alarm) {
        alarmsRepository.editAlarm(alarm)
    }
}
