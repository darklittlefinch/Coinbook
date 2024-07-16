package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.DebtDbModel
import com.elliemoritz.coinbook.data.util.formatTime
import com.elliemoritz.coinbook.data.util.parseTime
import com.elliemoritz.coinbook.domain.entities.Debt

class DebtMapper {

    fun mapEntityToDbModel(debt: Debt) = DebtDbModel(
        id = debt.id,
        amount = debt.amount,
        creditor = debt.creditor,
        deadline = formatTime(debt.deadline)
    )

    fun mapDbModelToEntity(dbModel: DebtDbModel) = Debt(
        id = dbModel.id,
        amount = dbModel.amount,
        creditor = dbModel.creditor,
        deadline = parseTime(dbModel.deadline)
    )

    fun mapListDbModelToListEntities(list: List<DebtDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}