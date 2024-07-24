package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryAndLimit(
    @Embedded
    val category: CategoryDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val limit: LimitDbModel
)
