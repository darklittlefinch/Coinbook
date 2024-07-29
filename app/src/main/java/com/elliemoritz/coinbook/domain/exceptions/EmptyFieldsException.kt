package com.elliemoritz.coinbook.domain.exceptions

class EmptyFieldsException(
    errorMessage: String = "Fields must not be empty"
) : RuntimeException(errorMessage)
