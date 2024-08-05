package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Debt

sealed class DebtsState {
    class NoData(val amount: String) : DebtsState()
    class Amount(val amount: String) : DebtsState()
    class DebtsList(val list: List<Debt>) : DebtsState()
    class Currency(val currency: String) : DebtsState()
    data object NotEnoughMoney : DebtsState()
}
