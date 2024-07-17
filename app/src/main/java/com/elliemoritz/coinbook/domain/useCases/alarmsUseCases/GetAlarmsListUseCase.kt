package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import javax.inject.Inject

class GetAlarmsListUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    fun getAlarmsList(): LiveData<List<Alarm>> {
        return alarmsRepository.getAlarmsList()
    }
}
