package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmsListUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    operator fun invoke(): Flow<List<Alarm>> {
        return alarmsRepository.getAlarmsList()
    }
}
