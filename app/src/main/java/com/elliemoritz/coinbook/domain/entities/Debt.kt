package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Debt(
    val amount: Int,
    val creditor: String,
    val startedMillis: Long,
    val finished: Boolean,
    val id: Long = UNDEFINED_ID
)
