package com.elliemoritz.coinbook.domain.entities

import java.sql.Timestamp

data class Alarm(
    val dateTime: Timestamp,
    var description: String,
    var amount: Int,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
