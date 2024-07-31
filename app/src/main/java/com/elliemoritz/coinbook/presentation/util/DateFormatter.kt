package com.elliemoritz.coinbook.presentation.util

import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun formatDate(timestamp: Timestamp): String {

    val sdf = SimpleDateFormat.getDateInstance()

    val calendar = Calendar.getInstance()
    sdf.timeZone = calendar.timeZone
    val formattedDate = sdf.format(timestamp)

    return formattedDate
}

fun formatTime(timestamp: Timestamp): String {

    val sdf = SimpleDateFormat.getTimeInstance()

    val calendar = Calendar.getInstance()
    sdf.timeZone = calendar.timeZone
    val formattedDate = sdf.format(timestamp)

    return formattedDate
}

