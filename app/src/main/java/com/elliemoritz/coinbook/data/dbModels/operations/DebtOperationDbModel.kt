package com.elliemoritz.coinbook.data.dbModels.operations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debt_operations")
data class DebtOperationDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Int,
    val type: String,
    val debtId: Long,
    val debtCreditor: String,
    val dateTimeMillis: Long,
)
