package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import java.sql.Timestamp

data class Alarm(
    val dateTime: Timestamp,
    var description: String,
    var amount: Int,
    val id: Int = UNDEFINED_ID
)