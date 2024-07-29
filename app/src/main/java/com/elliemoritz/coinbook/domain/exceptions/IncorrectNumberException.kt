package com.elliemoritz.coinbook.domain.exceptions

class IncorrectNumberException(
    errorMessage: String = "Field for numbers must not contain other characters"
) : RuntimeException(errorMessage)
