package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Alarm

interface AlarmsRepository {
    fun getAlarmsList(): LiveData<List<Alarm>>
    suspend fun getAlarm(id: Int): Alarm
    suspend fun addAlarm(alarm: Alarm)
    suspend fun editAlarm(alarm: Alarm)
    suspend fun removeAlarm(alarm: Alarm)
}