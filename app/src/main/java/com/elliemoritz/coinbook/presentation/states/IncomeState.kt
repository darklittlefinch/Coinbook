package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.operations.Income

sealed class IncomeState {
    data object NoData: IncomeState()
    class Amount(val amount: String) : IncomeState()
    class IncomeList(val list: List<Income>) : IncomeState()
    class Currency(val currency: String) : IncomeState()
}
