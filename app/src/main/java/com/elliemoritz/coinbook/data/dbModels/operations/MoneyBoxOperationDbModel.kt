package com.elliemoritz.coinbook.data.dbModels.operations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mb_operations")
data class MoneyBoxOperationDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Int,
    val type: String,
    val dateTimeMillis: Long,
    val currency: String
)
