package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import javax.inject.Inject

class MoneyBoxMapper @Inject constructor() {

    fun mapEntityToDbModel(moneyBox: MoneyBox) = MoneyBoxDbModel(
        id = moneyBox.id,
        goalAmount = moneyBox.goalAmount,
        totalAmount = moneyBox.totalAmount,
        goal = moneyBox.goal,
        startedMillis = moneyBox.startedMillis
    )

    fun mapDbModelToEntity(dbModel: MoneyBoxDbModel) = MoneyBox(
        id = dbModel.id,
        goalAmount = dbModel.goalAmount,
        totalAmount = dbModel.totalAmount,
        goal = dbModel.goal,
        startedMillis = dbModel.startedMillis
    )
}