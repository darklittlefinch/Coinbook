package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.ExpenseCategoryDbModel
import com.elliemoritz.coinbook.domain.entities.ExpenseCategory

class ExpenseCategoryMapper {

    fun mapEntityToDbModel(expenseCategory: ExpenseCategory) = ExpenseCategoryDbModel(
        id = expenseCategory.id,
        name = expenseCategory.name,
        amount = expenseCategory.amount,
        limit = expenseCategory.limit
    )

    fun mapDbModelToEntity(expenseCategoryDbModel: ExpenseCategoryDbModel) = ExpenseCategory(
        id = expenseCategoryDbModel.id,
        name = expenseCategoryDbModel.name,
        amount = expenseCategoryDbModel.amount,
        limit = expenseCategoryDbModel.limit
    )

    fun mapListDbModelToListEntities(list: List<ExpenseCategoryDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}