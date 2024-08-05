package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.operations.IncomeDbModel
import com.elliemoritz.coinbook.domain.entities.operations.Income
import javax.inject.Inject

class IncomeMapper @Inject constructor() {

    fun mapEntityToDbModel(entity: Income) = IncomeDbModel(
        id = entity.id,
        amount = entity.amount,
        source = entity.source,
        dateTimeMillis = entity.dateTimeMillis
    )

    fun mapDbModelToEntity(dbModel: IncomeDbModel) = Income(
        id = dbModel.id,
        amount = dbModel.amount,
        source = dbModel.source,
        dateTimeMillis = dbModel.dateTimeMillis
    )

    fun mapListDbModelToListEntities(list: List<IncomeDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
