package com.elliemoritz.coinbook.domain.exceptions

class LimitWithoutValueException(
    errorMessage: String = "Limit has no value"
) : RuntimeException(errorMessage)
