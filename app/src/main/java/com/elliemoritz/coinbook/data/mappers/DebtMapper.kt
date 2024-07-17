package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.DebtDbModel
import com.elliemoritz.coinbook.domain.entities.Debt
import java.sql.Timestamp

class DebtMapper {

    fun mapEntityToDbModel(debt: Debt) = DebtDbModel(
        id = debt.id,
        amount = debt.amount,
        creditor = debt.creditor,
        deadlineMillis = debt.deadline.time
    )

    fun mapDbModelToEntity(dbModel: DebtDbModel) = Debt(
        id = dbModel.id,
        amount = dbModel.amount,
        creditor = dbModel.creditor,
        deadline = Timestamp(dbModel.deadlineMillis)
    )

    fun mapListDbModelToListEntities(list: List<DebtDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}