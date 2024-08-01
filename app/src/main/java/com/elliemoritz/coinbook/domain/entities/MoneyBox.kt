package com.elliemoritz.coinbook.domain.entities

data class MoneyBox(
    var goalAmount: Int,
    var goal: String,
    val startedMillis: Long,
    var totalAmount: Int = 0,
    val id: Int = MONEY_BOX_ID
) {
    companion object {
        const val MONEY_BOX_ID = 1
    }
}
