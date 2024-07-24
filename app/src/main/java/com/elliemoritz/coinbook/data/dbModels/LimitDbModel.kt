package com.elliemoritz.coinbook.data.dbModels

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "limits",
    foreignKeys = [ForeignKey(
        entity = CategoryDbModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class LimitDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Int,
    val categoryId: Int
)
