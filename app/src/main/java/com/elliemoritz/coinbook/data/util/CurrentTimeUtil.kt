package com.elliemoritz.coinbook.data.util

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val FIRST_DAY_OF_MONTH = 1
private const val FIRST_HOUR_OF_MONTH = 0
private const val FIRST_MINUTE_OF_MONTH = 0

fun getBeginOfMonthMillis(): Long {
    val timestamp = getBeginOfMonthTimestamp()
    return timestamp.time
}

private fun getBeginOfMonthTimestamp(): Timestamp {
    val currentTime = LocalDateTime.now()
    val month = currentTime.monthValue
    val year = currentTime.year
    val beginOfMonthMillis = LocalDateTime.of(
        year,
        month,
        FIRST_DAY_OF_MONTH,
        FIRST_HOUR_OF_MONTH,
        FIRST_MINUTE_OF_MONTH
    ).toInstant(ZoneOffset.UTC).toEpochMilli()
    return Timestamp(beginOfMonthMillis)
}
