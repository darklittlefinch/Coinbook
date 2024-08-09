package com.elliemoritz.coinbook.domain.entities

data class MoneyBox(
    val goalAmount: Int,
    val goal: String,
    val startedMillis: Long,
    val totalAmount: Int = 0,
    val id: Long = MONEY_BOX_ID
) {
    companion object {
        const val MONEY_BOX_ID = 1L
    }
}
