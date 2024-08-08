package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.AlarmsDao
import com.elliemoritz.coinbook.data.mappers.AlarmMapper
import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlarmsRepositoryImpl @Inject constructor(
    private val dao: AlarmsDao,
    private val mapper: AlarmMapper
) : AlarmsRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getAlarmsList(): Flow<List<Alarm>> = flow {
        val dbModelsList = dao.getAlarmsList()
        val alarmsList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(alarmsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getAlarmsList()
            val updatedAlarmsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedAlarmsList)
        }
    }

    override fun getAlarm(id: Long): Flow<Alarm> = flow {
        val dbModel = dao.getAlarm(id)
        val alarm = mapper.mapDbModelToEntity(dbModel)
        emit(alarm)

        refreshEvents.collect {
            val updatedDbModel = dao.getAlarm(id)
            val updatedAlarm = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedAlarm)
        }
    }

    override suspend fun addAlarm(alarm: Alarm) {
        val dbModel = mapper.mapEntityToDbModel(alarm)
        dao.addAlarm(dbModel)
    }

    override suspend fun editAlarm(alarm: Alarm) {
        val dbModel = mapper.mapEntityToDbModel(alarm)
        dao.addAlarm(dbModel)
    }

    override suspend fun removeAlarm(alarm: Alarm) {
        dao.removeAlarm(alarm.id)
        refreshData()
    }

    override fun getAlarmsCount(): Flow<Long> = flow {
        val alarmsCount = dao.getAlarmsCount()
        emit(alarmsCount)

        refreshEvents.collect {
            val updatedAlarmsCount = dao.getAlarmsCount()
            emit(updatedAlarmsCount)
        }
    }
}