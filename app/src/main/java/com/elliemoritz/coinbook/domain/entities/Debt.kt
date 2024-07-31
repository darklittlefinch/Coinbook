package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import java.sql.Timestamp

data class Debt(
    var amount: Int,
    var creditor: String,
    val started: Timestamp,
    val id: Int = UNDEFINED_ID
)
