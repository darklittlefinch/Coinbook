package com.elliemoritz.coinbook.presentation.util

import java.sql.Timestamp

fun getCurrentTimestamp(): Timestamp {
    val currentTimeMillis = System.currentTimeMillis()
    return Timestamp(currentTimeMillis)
}