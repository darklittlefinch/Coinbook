package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class DebtOperation(
    val debtType: Type,
    val debtDateTimeMillis: Long,
    var debtAmount: Int,
    var debtCreditor: String,
    val debtId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.DEBT, debtType, debtDateTimeMillis, debtAmount, debtCreditor, debtId
)
