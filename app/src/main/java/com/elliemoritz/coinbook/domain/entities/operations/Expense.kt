package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Expense(
    val amount: Int,
    val categoryId: Int,
    val categoryName: String,
    val dateTimeMillis: Long,
    val id: Int = UNDEFINED_ID
)
