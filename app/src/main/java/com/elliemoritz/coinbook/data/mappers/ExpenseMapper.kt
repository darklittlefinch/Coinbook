package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.operations.ExpenseDbModel
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import javax.inject.Inject

class ExpenseMapper @Inject constructor() {

    fun mapEntityToDbModel(entity: Expense) = ExpenseDbModel(
        id = entity.id,
        amount = entity.amount,
        categoryId = entity.categoryId,
        categoryName = entity.categoryName,
        dateTimeMillis = entity.dateTimeMillis
    )

    fun mapDbModelToEntity(dbModel: ExpenseDbModel) = Expense(
        id = dbModel.id,
        amount = dbModel.amount,
        categoryId = dbModel.categoryId,
        categoryName = dbModel.categoryName,
        dateTimeMillis = dbModel.dateTimeMillis
    )

    fun mapListDbModelToListEntities(list: List<ExpenseDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
