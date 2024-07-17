package com.elliemoritz.coinbook.presentation.states

sealed class MainState

class MainData(
    balance: Int,
    income: Int,
    expenses: Int,
    moneyBoxAmount: Int,
    debtsAmount: Int,
    hasLimits: Boolean,
    hasAlarms: Boolean
) : HistoryState()
