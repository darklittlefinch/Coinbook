package com.elliemoritz.coinbook.domain.entities

import java.time.LocalDateTime

data class Operation(
    val operationForm: OperationForm,
    val type: Type,
    val date: LocalDateTime,
    var amount: Int,
    var additionalInfo: String = "",
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
