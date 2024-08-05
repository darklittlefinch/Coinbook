package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Debt(
    var amount: Int,
    var creditor: String,
    val startedMillis: Long,
    var finished: Boolean,
    val id: Int = UNDEFINED_ID
)
