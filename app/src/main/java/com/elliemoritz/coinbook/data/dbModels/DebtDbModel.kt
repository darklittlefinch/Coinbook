package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class DebtDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Int,
    val creditor: String,
    val startedMillis: Long
)
