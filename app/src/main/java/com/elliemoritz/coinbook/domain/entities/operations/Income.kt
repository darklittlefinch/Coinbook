package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Income(
    var amount: Int,
    var source: String,
    val dateTimeMillis: Long,
    val id: Int = UNDEFINED_ID
)
