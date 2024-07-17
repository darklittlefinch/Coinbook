package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class OperationDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val operationForm: String,
    val type: String,
    val dateTimeMillis: Long,
    val amount: Int,
    val info: String = ""
)
