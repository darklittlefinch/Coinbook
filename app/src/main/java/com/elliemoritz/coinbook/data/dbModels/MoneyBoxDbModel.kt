package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "money_box")
data class MoneyBoxDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val amount: Int,
    val goal: String,
    val deadlineMillis: Long
)
