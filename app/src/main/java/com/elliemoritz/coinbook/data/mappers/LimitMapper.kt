package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.LimitDbModel
import com.elliemoritz.coinbook.domain.entities.Limit
import javax.inject.Inject

class LimitMapper @Inject constructor() {

    fun mapEntityToDbModel(limit: Limit) = LimitDbModel(
        id = limit.id,
        limitAmount = limit.limitAmount,
        realAmount = limit.realAmount,
        categoryId = limit.categoryId,
        categoryName = limit.categoryName
    )

    fun mapDbModelToEntity(dbModel: LimitDbModel) = Limit(
        id = dbModel.id,
        limitAmount = dbModel.limitAmount,
        realAmount = dbModel.realAmount,
        categoryId = dbModel.categoryId,
        categoryName = dbModel.categoryName
    )

    fun mapListDbModelToListEntities(list: List<LimitDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}