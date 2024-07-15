package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elliemoritz.coinbook.domain.entities.ExpenseCategory

@Entity(tableName = "limits")
data class LimitDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val expenseCategory: ExpenseCategory,
    var amount: Int
)
