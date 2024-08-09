package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.operations.DebtOperationDbModel
import com.elliemoritz.coinbook.data.util.defineDbModelType
import com.elliemoritz.coinbook.data.util.defineEntityType
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import javax.inject.Inject

class DebtOperationsMapper @Inject constructor() {

    fun mapEntityToDbModel(entity: DebtOperation) = DebtOperationDbModel(
        id = entity.id,
        amount = entity.amount,
        type = defineDbModelType(entity.type),
        debtId = entity.debtId,
        debtCreditor = entity.debtCreditor,
        dateTimeMillis = entity.dateTimeMillis,
        currency = entity.currency
    )

    fun mapDbModelToEntity(dbModel: DebtOperationDbModel) = DebtOperation(
        id = dbModel.id,
        amount = dbModel.amount,
        type = defineEntityType(dbModel.type),
        debtId = dbModel.debtId,
        debtCreditor = dbModel.debtCreditor,
        dateTimeMillis = dbModel.dateTimeMillis,
        currency = dbModel.currency
    )

    fun mapListDbModelToListEntities(list: List<DebtOperationDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
