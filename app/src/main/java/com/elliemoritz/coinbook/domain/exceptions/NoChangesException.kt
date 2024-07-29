package com.elliemoritz.coinbook.domain.exceptions

class NoChangesException(
    errorMessage: String = "Nothing has changed"
) : RuntimeException(errorMessage)
