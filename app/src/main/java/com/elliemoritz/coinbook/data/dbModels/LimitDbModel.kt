package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elliemoritz.coinbook.domain.entities.Category

@Entity(tableName = "limits")
data class LimitDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val category: Category,
    val amount: Int
)
