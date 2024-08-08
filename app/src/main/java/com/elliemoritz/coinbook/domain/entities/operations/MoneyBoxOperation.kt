package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class MoneyBoxOperation(
    val amount: Int,
    val type: Type,
    val dateTimeMillis: Long,
    val id: Long = UNDEFINED_ID
)
