package com.elliemoritz.coinbook.domain.exceptions

class NotEnoughMoneyException(
    errorMessage: String = "Not enough money to remove this amount"
) : RuntimeException(errorMessage)
