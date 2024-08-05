package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type

/**
 * Class Operation is needed for creating list of all operation types for HistoryActivity.
 */

class Operation(
    val operationForm: OperationForm,
    val type: Type,
    var amount: Int,
    val dateTimeMillis: Long,
    val operationId: Int,

    /**
     * The field "info" in Operation class must contain information for HistoryActivity.
     *
     * For Income it will be source of money,
     * for Expense - category name,
     * for MoneyBoxOperation - description (see in string resources),
     * for Debt - creditor.
     */

    var info: String = ""
)
