package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.DebtDbModel
import com.elliemoritz.coinbook.domain.entities.Debt
import javax.inject.Inject

class DebtMapper @Inject constructor() {

    fun mapEntityToDbModel(debt: Debt) = DebtDbModel(
        id = debt.id,
        amount = debt.amount,
        creditor = debt.creditor
    )

    fun mapDbModelToEntity(dbModel: DebtDbModel) = Debt(
        id = dbModel.id,
        amount = dbModel.amount,
        creditor = dbModel.creditor
    )

    fun mapListDbModelToListEntities(list: List<DebtDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}