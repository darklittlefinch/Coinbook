package com.elliemoritz.coinbook.domain.entities

data class ExpenseCategory(
    val name: String,
    var amount: Int,
    var limit: Int,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
