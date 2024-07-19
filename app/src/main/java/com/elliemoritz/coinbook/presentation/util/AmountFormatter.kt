package com.elliemoritz.coinbook.presentation.util

private const val NUMBER_FORMAT_STEP = 1000
private const val MIN_STRING_LENGTH = 3
private const val DELIMITER = " "

fun formatAmount(amount: Int, currency: String): String {

    val result = StringBuilder()

    if (amount < NUMBER_FORMAT_STEP) {
        result.append(amount)
    } else {

        var divident = amount

        while (divident > 0) {
            if (result.capacity() > 0) {
                result.insert(0, DELIMITER)
            }

            val remainder = divident % NUMBER_FORMAT_STEP
            divident /= NUMBER_FORMAT_STEP

            result.insert(0, remainder)

            val length = remainder.toString().length

            if (divident != 0 && length < MIN_STRING_LENGTH) {
                for (i in length + 1..MIN_STRING_LENGTH) {
                    result.insert(0, 0)
                }
            }
        }
    }

    result.append(DELIMITER)
    result.append(currency)
    return result.toString()
}
