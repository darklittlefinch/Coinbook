package com.elliemoritz.coinbook.data.dbModels.operations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Int,
    val categoryId: Long,
    val categoryName: String,
    val dateTimeMillis: Long
)
