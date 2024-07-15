package com.elliemoritz.coinbook.domain.entities

import java.time.LocalDateTime

data class MoneyBox(
    var amount: Int,
    var goal: String,
    var deadline: LocalDateTime,
    val id: Int = MONEY_BOX_ID
) {
    companion object {
        const val MONEY_BOX_ID = 1
    }
}
