package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel
import com.elliemoritz.coinbook.domain.entities.MoneyBox

class MoneyBoxMapper {

    fun mapEntityToDbModel(moneyBox: MoneyBox) = MoneyBoxDbModel(
        id = moneyBox.id,
        amount = moneyBox.amount,
        goal = moneyBox.goal,
        deadline = moneyBox.deadline
    )

    fun mapDbModelToEntity(dbModel: MoneyBoxDbModel) = MoneyBox(
        id = dbModel.id,
        amount = dbModel.amount,
        goal = dbModel.goal,
        deadline = dbModel.deadline
    )
}