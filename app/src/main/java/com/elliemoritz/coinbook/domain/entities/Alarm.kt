package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Alarm(
    val dateTimeMillis: Long,
    val description: String,
    val amount: Int,
    val currency: String,
    val id: Long = UNDEFINED_ID
)
