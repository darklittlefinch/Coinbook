package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.LimitDbModel
import com.elliemoritz.coinbook.domain.entities.Limit

class LimitMapper {

    fun mapEntityToDbModel(limit: Limit) = LimitDbModel(
        id = limit.id,
        categoryName = limit.categoryName,
        amount = limit.amount
    )

    fun mapDbModelToEntity(dbModel: LimitDbModel) = Limit(
        id = dbModel.id,
        categoryName = dbModel.categoryName,
        amount = dbModel.amount
    )

    fun mapListDbModelToListEntities(list: List<LimitDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}