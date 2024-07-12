package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository

class EditAlarmUseCase(
    private val alarmsRepository: AlarmsRepository
) {
    fun editAlarm(alarm: Alarm) {
        alarmsRepository.editAlarm(alarm)
    }
}
