package com.elliemoritz.coinbook.presentation.util

import com.elliemoritz.coinbook.domain.entities.helpers.Type

private const val NUMBER_FORMAT_STEP = 1000
private const val MIN_STRING_LENGTH = 3
private const val DELIMITER = " "

private const val ADD_SIGN = "+"
private const val SUBTRACT_SIGN = "-"

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

fun formatAmountWithSign(amount: Int, currency: String, type: Type): String {
    val amountWithCurrency = formatAmount(amount, currency)

    val sign = when (type) {
        Type.INCOME -> ADD_SIGN
        Type.EXPENSE -> SUBTRACT_SIGN
    }

    return sign + DELIMITER + amountWithCurrency
}
