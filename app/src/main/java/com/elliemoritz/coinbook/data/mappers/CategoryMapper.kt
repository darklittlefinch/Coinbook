package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.CategoryDbModel
import com.elliemoritz.coinbook.domain.entities.Category

class CategoryMapper {

    fun mapEntityToDbModel(category: Category) = CategoryDbModel(
        id = category.id,
        name = category.name,
        amount = category.amount,
        limit = category.limit
    )

    fun mapDbModelToEntity(categoryDbModel: CategoryDbModel) = Category(
        id = categoryDbModel.id,
        name = categoryDbModel.name,
        amount = categoryDbModel.amount,
        limit = categoryDbModel.limit
    )

    fun mapListDbModelToListEntities(list: List<CategoryDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}