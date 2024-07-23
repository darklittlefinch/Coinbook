package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import java.sql.Timestamp

data class DebtOperation(
    val debtType: Type,
    val debtDate: Timestamp,
    var debtAmount: Int,
    var debtCreditor: String,
    val debtId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.DEBT, debtType, debtDate, debtAmount, debtCreditor, debtId
)
