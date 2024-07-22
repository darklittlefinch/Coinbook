package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    operator fun invoke(id: Int): Flow<Alarm> {
        return alarmsRepository.getAlarm(id)
    }
}
