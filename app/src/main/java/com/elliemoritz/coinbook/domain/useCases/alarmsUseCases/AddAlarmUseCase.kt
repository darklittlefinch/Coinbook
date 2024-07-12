package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository

class AddAlarmUseCase(
    private val alarmsRepository: AlarmsRepository
) {
    fun addAlarm(alarm: Alarm) {
        alarmsRepository.addAlarm(alarm)
    }
}
