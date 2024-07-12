package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.ExpenseCategory
import com.elliemoritz.coinbook.domain.entities.OperationForm
import com.elliemoritz.coinbook.domain.entities.Type
import java.time.LocalDateTime

data class Expense(
    val expDate: LocalDateTime,
    var expAmount: Int,
    var expCategory: ExpenseCategory,
    val expId: Int = UNDEFINED_ID
) : Operation(
    OperationForm.EXPENSE, Type.EXPENSE, expDate, expAmount, expCategory.name, expId
)
