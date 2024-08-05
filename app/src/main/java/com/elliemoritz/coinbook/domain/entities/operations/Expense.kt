package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Expense(
    var amount: Int,
    var categoryId: Int,
    var categoryName: String,
    val dateTimeMillis: Long,
    val id: Int = UNDEFINED_ID
)
