package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Limit(
    val limitAmount: Int,
    val realAmount: Int,
    val categoryId: Long,
    val categoryName: String,
    val currency: String,
    val id: Long = UNDEFINED_ID
)
