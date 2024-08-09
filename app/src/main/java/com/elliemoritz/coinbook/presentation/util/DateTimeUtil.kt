package com.elliemoritz.coinbook.presentation.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun formatDate(dateTimeMillis: Long): String {

    val dateTime = getZonedDateTime(dateTimeMillis)
    val date = dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    return date
}

fun formatTime(dateTimeMillis: Long): String {

    val dateTime = getZonedDateTime(dateTimeMillis)
    val time = dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    return time
}

private fun getZonedDateTime(dateTimeMillis: Long): ZonedDateTime {
    val instant = Instant.ofEpochMilli(dateTimeMillis)
    val dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    return dateTime
}

fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}
