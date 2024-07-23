package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import java.sql.Timestamp

data class MoneyBoxOperation(
    val mbType: Type,
    val mbDate: Timestamp,
    var mbAmount: Int,
    val mbId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.MONEY_BOX, mbType, mbDate, mbAmount, id = mbId
)
