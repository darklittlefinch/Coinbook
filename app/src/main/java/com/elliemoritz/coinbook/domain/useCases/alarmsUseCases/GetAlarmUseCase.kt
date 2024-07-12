package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository

class GetAlarmUseCase(
    private val alarmsRepository: AlarmsRepository
) {
    fun getAlarm(id: Int): Alarm {
        return alarmsRepository.getAlarm(id)
    }
}
