package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class DebtOperation(
    var amount: Int,
    val type: Type,
    val debtId: Int,
    val debtCreditor: String,
    val dateTimeMillis: Long,
    val id: Int = UNDEFINED_ID,
)
