package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Alarm(
    val dateTimeMillis: Long,
    var description: String,
    var amount: Int,
    val id: Int = UNDEFINED_ID
)