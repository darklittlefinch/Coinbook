package com.elliemoritz.coinbook.domain.entities

import java.time.LocalDateTime

data class Alarm(
    val dateTime: LocalDateTime,
    var description: String,
    var amount: Int,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
