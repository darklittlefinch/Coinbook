package com.elliemoritz.coinbook.presentation.states

sealed class MainState {
    data object StartLoading : MainState()
    data object EndLoading : MainState()
    class Balance(val amount: String) : MainState()
    class Income(val amount: String) : MainState()
    class Expenses(val amount: String) : MainState()
    class MoneyBox(val amount: String, val wasStarted: Boolean) : MainState()
    class Debts(val amount: String, val userHasDebts: Boolean) : MainState()
    class Limits(val userHasLimits: Boolean) : MainState()
    class Alarms(val userHasAlarms: Boolean) : MainState()
}
