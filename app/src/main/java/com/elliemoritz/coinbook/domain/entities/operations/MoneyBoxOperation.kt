package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class MoneyBoxOperation(
    val mbType: Type,
    val mbDateTimeMillis: Long,
    var mbAmount: Int,
    val mbId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.MONEY_BOX, mbType, mbDateTimeMillis, mbAmount, id = mbId
)
