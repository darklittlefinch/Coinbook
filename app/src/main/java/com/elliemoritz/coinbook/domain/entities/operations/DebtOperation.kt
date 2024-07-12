package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.OperationForm
import com.elliemoritz.coinbook.domain.entities.Type
import java.time.LocalDateTime

data class DebtOperation(
    val debtType: Type,
    val debtDate: LocalDateTime,
    var debtAmount: Int,
    var debtCreditor: String,
    val debtId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.DEBT, debtType, debtDate, debtAmount, debtCreditor, debtId
)
