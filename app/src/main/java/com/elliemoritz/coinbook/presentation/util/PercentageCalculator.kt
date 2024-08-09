package com.elliemoritz.coinbook.presentation.util

private const val HUNDRED_PERCENT = 100

fun calculatePercent(first: Int, second: Int): Int {
    return first / second * HUNDRED_PERCENT
}
