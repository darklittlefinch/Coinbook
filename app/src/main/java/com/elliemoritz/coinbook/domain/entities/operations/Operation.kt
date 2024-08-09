package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type

/**
 * Class Operation is needed for creating list of all operation types for HistoryActivity.
 */

data class Operation(
    val operationForm: OperationForm,
    val type: Type,
    val amount: Int,
    val dateTimeMillis: Long,
    val operationId: Long,
    val currency: String,

    /**
     * The field "info" in Operation class must contain information for HistoryActivity.
     *
     * For Income it will be source of money,
     * for Expense - category name,
     * for MoneyBoxOperation - description (see in string resources),
     * for Debt - creditor.
     */

    val info: String = ""
)
