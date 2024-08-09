package com.elliemoritz.coinbook.domain.entities

import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID

data class Category(
    val name: String,
    val id: Long = UNDEFINED_ID
)