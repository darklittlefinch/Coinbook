package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class ExpenseCategoryDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    var amount: Int,
    var limit: Int
)
