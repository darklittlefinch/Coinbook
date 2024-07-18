package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import java.sql.Timestamp
import javax.inject.Inject

class MoneyBoxMapper @Inject constructor() {

    fun mapEntityToDbModel(moneyBox: MoneyBox) = MoneyBoxDbModel(
        id = moneyBox.id,
        amount = moneyBox.amount,
        goal = moneyBox.goal,
        deadlineMillis = moneyBox.deadline.time,
        startedMillis = moneyBox.started.time
    )

    fun mapDbModelToEntity(dbModel: MoneyBoxDbModel) = MoneyBox(
        id = dbModel.id,
        amount = dbModel.amount,
        goal = dbModel.goal,
        deadline = Timestamp(dbModel.deadlineMillis),
        started = Timestamp(dbModel.startedMillis)
    )
}