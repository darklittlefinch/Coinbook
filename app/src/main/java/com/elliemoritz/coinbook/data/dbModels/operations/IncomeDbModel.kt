package com.elliemoritz.coinbook.data.dbModels.operations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class IncomeDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Int,
    val source: String,
    val dateTimeMillis: Long
)
