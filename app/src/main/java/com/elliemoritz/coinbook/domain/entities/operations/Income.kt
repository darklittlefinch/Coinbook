package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import java.time.LocalDateTime

data class Income(
    val incDate: LocalDateTime,
    var incAmount: Int,
    var incSource: String,
    val incId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.INCOME, Type.INCOME, incDate, incAmount, incSource, incId
)
