package com.elliemoritz.coinbook.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.elliemoritz.coinbook.data.dao.AlarmsDao
import com.elliemoritz.coinbook.data.mappers.AlarmMapper
import com.elliemoritz.coinbook.domain.entities.Alarm
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository

class AlarmsRepositoryImpl(
    private val dao: AlarmsDao,
    private val mapper: AlarmMapper
) : AlarmsRepository {

    override fun getAlarmsList(): LiveData<List<Alarm>> {
        val alarmsLiveData = dao.getAlarmsList()
        return alarmsLiveData.map {
            mapper.mapListDbModelToListEntities(it)
        }
    }

    override fun getAlarm(id: Int): Alarm {
        val dbModel = dao.getAlarm(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun addAlarm(alarm: Alarm) {
        val dbModel = mapper.mapEntityToDbModel(alarm)
        dao.addAlarm(dbModel)
    }

    override fun editAlarm(alarm: Alarm) {
        val dbModel = mapper.mapEntityToDbModel(alarm)
        dao.addAlarm(dbModel)
    }

    override fun removeAlarm(alarm: Alarm) {
        dao.removeAlarm(alarm.id)
    }
}