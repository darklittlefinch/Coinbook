package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Category(
    val name: String,
    var amount: Int,
    var limit: Int,
    val id: Int = UNDEFINED_ID
)