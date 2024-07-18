package com.elliemoritz.coinbook.presentation.states

sealed class MainState

class MainData(
    val balance: String,
    val income: String,
    val expenses: String,
    val hasMoneyBox: Boolean,
    val moneyBoxAmount: String,
    val hasDebts: Boolean,
    val debtsAmount: String,
    val hasLimits: Boolean,
    val hasAlarms: Boolean
) : MainState()
