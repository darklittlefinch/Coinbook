package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "money_box")
data class MoneyBoxDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val amount: Int,
    val goal: String,
    val deadline: String
)
