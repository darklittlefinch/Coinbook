package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation

sealed class MoneyBoxState {
    data object NoMoneyBox : MoneyBoxState()
    class TotalAmount(val amount: String) : MoneyBoxState()
    class OperationsList(val list: List<MoneyBoxOperation>) : MoneyBoxState()
    class Goal(val goalAmount: String, val goal: String) : MoneyBoxState()
    class Currency(val currency: String) : MoneyBoxState()
}
