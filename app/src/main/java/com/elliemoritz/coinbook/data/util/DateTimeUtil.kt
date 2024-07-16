package com.elliemoritz.coinbook.data.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val DEFAULT_TIME = LocalDateTime.of(0, 0, 0, 0, 0)

fun formatTime(localDateTime: LocalDateTime): String {
    return localDateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME)
}

fun parseTime(string: String): LocalDateTime {
    val result = try {
        LocalDateTime.parse(string, DateTimeFormatter.RFC_1123_DATE_TIME)
    } catch (e: DateTimeParseException) {
        DEFAULT_TIME
    }

    return result
}
