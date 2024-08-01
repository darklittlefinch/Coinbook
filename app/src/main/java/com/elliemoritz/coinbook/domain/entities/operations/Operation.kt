package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

abstract class Operation(
    val operationForm: OperationForm,
    val type: Type,
    val dateTimeMillis: Long,
    var amount: Int,
    var info: String = "",
    val id: Int = UNDEFINED_ID
)
