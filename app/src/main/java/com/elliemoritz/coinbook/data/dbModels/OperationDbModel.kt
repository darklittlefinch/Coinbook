package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import java.time.LocalDateTime

@Entity(tableName = "operations")
data class OperationDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val operationForm: String,
    val type: String,
    val date: String,
    val amount: Int,
    val info: String = ""
)
