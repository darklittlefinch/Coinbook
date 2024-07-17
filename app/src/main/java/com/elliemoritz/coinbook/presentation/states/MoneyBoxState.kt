package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation

sealed class MoneyBoxState

data object NoMoneyBox : MoneyBoxState()

class NoMoneyBoxData(
    goalAmount: Int,
    goal: String,
    deadline: String,
    dailyPayment: Int,
    totalAmount: Int = 0
) : MoneyBoxState()

class MoneyBoxData(
    totalAmount: Int,
    goalAmount: Int,
    goal: String,
    deadline: String,
    dailyPayment: Int,
    operationsHistory: List<MoneyBoxOperation>
) : MoneyBoxState()

class MoneyBoxCompleted(
    totalAmount: Int,
    goalAmount: Int,
    goal: String,
    deadline: String,
    operationsHistory: List<MoneyBoxOperation>
) : MoneyBoxState()

class MoneyBoxExceeded(
    totalAmount: Int,
    goalAmount: Int,
    goal: String,
    deadline: String,
    operationsHistory: List<MoneyBoxOperation>
) : MoneyBoxState()
