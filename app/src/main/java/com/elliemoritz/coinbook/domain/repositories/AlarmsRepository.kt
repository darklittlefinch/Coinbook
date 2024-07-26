package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmsRepository {
    fun getAlarmsList(): Flow<List<Alarm>>
    fun getAlarm(id: Int): Flow<Alarm>
    suspend fun addAlarm(alarm: Alarm)
    suspend fun editAlarm(alarm: Alarm)
    suspend fun removeAlarm(alarm: Alarm)
    fun getAlarmsCount(): Flow<Int>
}