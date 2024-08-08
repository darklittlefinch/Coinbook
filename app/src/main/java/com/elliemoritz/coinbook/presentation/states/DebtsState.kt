package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Debt

sealed class DebtsState {
    data object NoData : DebtsState()
    data object HasData : DebtsState()
    class Amount(val amount: String) : DebtsState()
    class DebtsList(val list: List<Debt>) : DebtsState()
    data object NotEnoughMoney : DebtsState()
}
