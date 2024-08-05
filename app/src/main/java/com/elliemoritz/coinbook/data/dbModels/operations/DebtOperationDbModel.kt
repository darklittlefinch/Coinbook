package com.elliemoritz.coinbook.data.dbModels.operations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debt_operations")
data class DebtOperationDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Int,
    val type: String,
    val debtId: Int,
    val debtCreditor: String,
    val dateTimeMillis: Long,
)
