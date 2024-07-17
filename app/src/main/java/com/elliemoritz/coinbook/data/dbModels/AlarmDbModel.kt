package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "alarms")
data class AlarmDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dateTimeMillis: Long,
    val description: String,
    val amount: Int
)
