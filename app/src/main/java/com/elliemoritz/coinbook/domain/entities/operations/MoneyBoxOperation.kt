package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.OperationForm
import com.elliemoritz.coinbook.domain.entities.Type
import java.time.LocalDateTime

data class MoneyBoxOperation(
    val mbType: Type,
    val mbDate: LocalDateTime,
    var mbAmount: Int,
    val mbId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.MONEY_BOX, mbType, mbDate, mbAmount, id = mbId
)
