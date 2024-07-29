package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.LimitDbModel
import com.elliemoritz.coinbook.domain.entities.Limit
import javax.inject.Inject

class LimitMapper @Inject constructor() {

    fun mapEntityToDbModel(limit: Limit) = LimitDbModel(
        id = limit.id,
        amount = limit.amount,
        categoryId = limit.categoryId
    )

    fun mapDbModelToEntity(dbModel: LimitDbModel) = Limit(
        id = dbModel.id,
        amount = dbModel.amount,
        categoryId = dbModel.categoryId
    )

    fun mapListDbModelToListEntities(list: List<LimitDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}