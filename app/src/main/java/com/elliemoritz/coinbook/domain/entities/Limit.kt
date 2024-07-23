package com.elliemoritz.coinbook.domain.entities

data class Limit(
    val categoryName: String,
    var amount: Int,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
