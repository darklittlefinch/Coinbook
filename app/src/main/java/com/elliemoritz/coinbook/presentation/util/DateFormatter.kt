package com.elliemoritz.coinbook.presentation.util

import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun formatDate(timestamp: Timestamp): String {

    val dateTime = getZonedDateTime(timestamp)
    val date = dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    return date
}

fun formatTime(timestamp: Timestamp): String {

    val dateTime = getZonedDateTime(timestamp)
    val time = dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    return time
}

private fun getZonedDateTime(timestamp: Timestamp): ZonedDateTime {
    val instant = Instant.ofEpochMilli(timestamp.time)
    val dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    return dateTime
}
