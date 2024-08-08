package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Income(
    val amount: Int,
    val source: String,
    val dateTimeMillis: Long,
    val id: Int = UNDEFINED_ID
)
