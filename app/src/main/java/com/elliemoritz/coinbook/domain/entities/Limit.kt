package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Limit(
    var amount: Int,
    val categoryId: Int,
    val id: Int = UNDEFINED_ID
)
