package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.AlarmDbModel
import com.elliemoritz.coinbook.domain.entities.Alarm
import java.sql.Timestamp

class AlarmMapper {

    fun mapEntityToDbModel(alarm: Alarm) = AlarmDbModel(
        id = alarm.id,
        dateTimeMillis = alarm.dateTime.time,
        description = alarm.description,
        amount = alarm.amount
    )

    fun mapDbModelToEntity(dbModel: AlarmDbModel) = Alarm(
        id = dbModel.id,
        dateTime = Timestamp(dbModel.dateTimeMillis),
        description = dbModel.description,
        amount = dbModel.amount
    )

    fun mapListDbModelToListEntities(list: List<AlarmDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
