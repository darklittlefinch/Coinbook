package com.elliemoritz.coinbook.domain.entities.operations

import com.elliemoritz.coinbook.domain.entities.OperationForm
import com.elliemoritz.coinbook.domain.entities.Type
import java.time.LocalDateTime

abstract class Operation(
    val operationForm: OperationForm,
    val type: Type,
    val date: LocalDateTime,
    var amount: Int,
    var info: String = "",
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
