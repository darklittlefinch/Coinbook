package com.elliemoritz.coinbook.domain.entities

import java.time.LocalDateTime

data class Debt(
    var amount: Int,
    var creditor: String,
    var deadline: LocalDateTime,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
