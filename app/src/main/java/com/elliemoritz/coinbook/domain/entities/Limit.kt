package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Limit(
    val limitAmount: Int,
    val realAmount: Int,
    val categoryId: Int,
    val categoryName: String,
    val id: Int = UNDEFINED_ID
)
