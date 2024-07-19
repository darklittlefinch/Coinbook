package com.elliemoritz.coinbook.presentation.util

private const val NUMBER_FORMAT_STEP = 1000
private const val DELIMITER = " "

fun formatAmount(amount: Int): String {

    if (amount < NUMBER_FORMAT_STEP) {
        return amount.toString()
    }

    var divident = amount
    val result = StringBuilder()

    while (divident > 0) {
        if (result.capacity() > 0) {
            result.insert(0, DELIMITER)
        }
        val remainder = divident % NUMBER_FORMAT_STEP
        result.insert(0, remainder)
        divident /= NUMBER_FORMAT_STEP
    }

    return result.toString()
}
