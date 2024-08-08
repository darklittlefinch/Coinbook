package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Expense(
    val amount: Int,
    val categoryId: Long,
    val categoryName: String,
    val dateTimeMillis: Long,
    val currency: String,
    val id: Long = UNDEFINED_ID
)
