package com.elliemoritz.coinbook.domain.entities

import java.sql.Timestamp

data class Debt(
    var amount: Int,
    var creditor: String,
    var deadline: Timestamp,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
