package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "limits")
data class LimitDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Int,
    val categoryId: Int
)
