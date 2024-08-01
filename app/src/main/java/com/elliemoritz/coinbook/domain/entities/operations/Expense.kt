package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Expense(
    val expDateTimeMillis: Long,
    var expAmount: Int,
    var expCategoryName: String,
    val expId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.EXPENSE, Type.EXPENSE, expDateTimeMillis, expAmount, expCategoryName, expId
)
