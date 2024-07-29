package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import java.sql.Timestamp

data class Income(
    val incDate: Timestamp,
    var incAmount: Int,
    var incSource: String,
    val incId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.INCOME, Type.INCOME, incDate, incAmount, incSource, incId
)
