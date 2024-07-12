package com.elliemoritz.coinbook.domain.entities

data class Limit(
    val expenseCategory: ExpenseCategory,
    var amount: Int,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
