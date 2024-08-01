package com.elliemoritz.coinbook.presentation.util

import androidx.core.text.isDigitsOnly
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.exceptions.NotEnoughMoneyException

fun checkEmptyFields(vararg fields: String) {
    for (field in fields) {
        if (field.isBlank()) {
            throw EmptyFieldsException()
        }
    }
}

fun checkIncorrectNumbers(vararg numbers: String) {
    for (number in numbers) {
        if (number.isNotBlank() && !number.isDigitsOnly()) {
            throw IncorrectNumberException()
        }
    }
}

fun checkNoChanges(newList: List<Any>, oldList: List<Any>) {
    if (newList.size != oldList.size) {
        throw RuntimeException(
            "checkNowChanges: size of lists is not the same. " +
                    "Check all arguments you've pass into this method"
        )
    }
    for (index in newList.indices) {
        if (newList[index] != oldList[index]) {
            return
        }
    }

    throw NoChangesException()
}

fun checkNotEnoughMoney(amount: Int, balance: Int) {
    if (amount > balance) {
        throw NotEnoughMoneyException()
    }
}
