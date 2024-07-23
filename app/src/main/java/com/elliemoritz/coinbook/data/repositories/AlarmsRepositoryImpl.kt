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

    private val refreshListEvents = MutableSharedFlow<Unit>()
    private val refreshAlarmEvents = MutableSharedFlow<Unit>()
    private val refreshCountEvents = MutableSharedFlow<Unit>()

    override fun getAlarmsList(): Flow<List<Alarm>> = flow {
        val list = dao.getAlarmsList()
        val result = mapper.mapListDbModelToListEntities(list)
        emit(result)
        refreshListEvents.collect {
            val updatedList = dao.getAlarmsList()
            val updatedResult = mapper.mapListDbModelToListEntities(updatedList)
            emit(updatedResult)
        }
    }

    override fun getAlarm(id: Int): Flow<Alarm> = flow {
        val dbModel = dao.getAlarm(id)
        val result = mapper.mapDbModelToEntity(dbModel)
        emit(result)
        refreshAlarmEvents.collect {
            val updatedDbModel = dao.getAlarm(id)
            val updatedResult = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedResult)
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
    }

    override fun getAlarmsCount(): Flow<Int> = flow {
        val result = dao.getAlarmsCount()
        emit(result)
        refreshCountEvents.collect {
            val updatedResult = dao.getAlarmsCount()
            emit(updatedResult)
        }
    }

    override suspend fun refreshAlarmsData() {
        refreshListEvents.emit(Unit)
        refreshAlarmEvents.emit(Unit)
        refreshCountEvents.emit(Unit)
    }
}