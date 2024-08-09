package com.elliemoritz.coinbook.data.util

import java.time.ZoneId
import java.time.ZonedDateTime

private const val FIRST_DAY_OF_MONTH = 1
private const val FIRST_HOUR_OF_MONTH = 0
private const val FIRST_MINUTE_OF_MONTH = 0

fun getBeginOfMonthMillis(): Long {

    return ZonedDateTime.now(ZoneId.systemDefault())
        .withMonth(FIRST_DAY_OF_MONTH)
        .withHour(FIRST_HOUR_OF_MONTH)
        .withMinute(FIRST_MINUTE_OF_MONTH)
        .toInstant()
        .toEpochMilli()
}
