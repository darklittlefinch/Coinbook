package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Limit(
    val categoryName: String,
    var amount: Int,
    val id: Int = UNDEFINED_ID
)
