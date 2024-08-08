package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class DebtOperation(
    val amount: Int,
    val type: Type,
    val debtId: Long,
    val debtCreditor: String,
    val dateTimeMillis: Long,
    val currency: String,
    val id: Long = UNDEFINED_ID,
)
