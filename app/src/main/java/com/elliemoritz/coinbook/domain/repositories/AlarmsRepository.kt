package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Alarm

interface AlarmsRepository {
    fun getAlarmsList(): LiveData<List<Alarm>>
    fun getAlarm(id: Int): Alarm
    fun addAlarm(operation: Alarm)
    fun editAlarm(operation: Alarm)
    fun removeAlarm(operation: Alarm)
}