package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.ExpenseCategory
import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import java.time.LocalDateTime

data class Expense(
    val expDate: LocalDateTime,
    var expAmount: Int,
    var expCategoryName: String,
    val expId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.EXPENSE, Type.EXPENSE, expDate, expAmount, expCategoryName, expId
)
