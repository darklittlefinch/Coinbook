package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.operations.MoneyBoxOperationDbModel
import com.elliemoritz.coinbook.data.util.defineDbModelType
import com.elliemoritz.coinbook.data.util.defineEntityType
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import javax.inject.Inject

class MoneyBoxOperationsMapper @Inject constructor() {

    fun mapEntityToDbModel(entity: MoneyBoxOperation) = MoneyBoxOperationDbModel(
        id = entity.id,
        amount = entity.amount,
        type = defineDbModelType(entity.type),
        dateTimeMillis = entity.dateTimeMillis,
        currency = entity.currency
    )

    fun mapDbModelToEntity(dbModel: MoneyBoxOperationDbModel) = MoneyBoxOperation(
        id = dbModel.id,
        amount = dbModel.amount,
        type = defineEntityType(dbModel.type),
        dateTimeMillis = dbModel.dateTimeMillis,
        currency = dbModel.currency
    )

    fun mapListDbModelToListEntities(list: List<MoneyBoxOperationDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
