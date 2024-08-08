package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class DebtDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Int,
    val creditor: String,
    val finished: Boolean,
    val startedMillis: Long
)
