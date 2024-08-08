package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.AlarmDbModel
import com.elliemoritz.coinbook.domain.entities.Alarm
import javax.inject.Inject

class AlarmMapper @Inject constructor() {

    fun mapEntityToDbModel(alarm: Alarm) = AlarmDbModel(
        id = alarm.id,
        dateTimeMillis = alarm.dateTimeMillis,
        description = alarm.description,
        amount = alarm.amount,
        currency = alarm.currency
    )

    fun mapDbModelToEntity(dbModel: AlarmDbModel) = Alarm(
        id = dbModel.id,
        dateTimeMillis = dbModel.dateTimeMillis,
        description = dbModel.description,
        amount = dbModel.amount,
        currency = dbModel.currency
    )

    fun mapListDbModelToListEntities(list: List<AlarmDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
