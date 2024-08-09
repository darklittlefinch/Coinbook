package com.elliemoritz.coinbook.data.util

import com.elliemoritz.coinbook.domain.entities.helpers.Type

const val TYPE_INCOME = "income"
const val TYPE_EXPENSE = "expense"

fun defineDbModelType(type: Type): String {
    return when (type) {
        Type.INCOME -> TYPE_INCOME
        Type.EXPENSE -> TYPE_EXPENSE
    }
}

fun defineEntityType(string: String): Type {
    return when (string) {
        TYPE_INCOME -> Type.INCOME
        TYPE_EXPENSE -> Type.EXPENSE
        else -> {
            throw RuntimeException("TypeDefiner: Unknown type")
        }
    }
}
