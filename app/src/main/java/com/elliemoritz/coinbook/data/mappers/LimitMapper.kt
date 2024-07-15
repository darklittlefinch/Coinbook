package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.LimitDbModel
import com.elliemoritz.coinbook.domain.entities.Limit

class LimitMapper {

    fun mapEntityToDbModel(limit: Limit) = LimitDbModel(
        id = limit.id,
        expenseCategory = limit.expenseCategory,
        amount = limit.amount
    )

    fun mapDbModelToEntity(dbModel: LimitDbModel) = Limit(
        id = dbModel.id,
        expenseCategory = dbModel.expenseCategory,
        amount = dbModel.amount
    )

    fun mapListDbModelToListEntities(list: List<LimitDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}