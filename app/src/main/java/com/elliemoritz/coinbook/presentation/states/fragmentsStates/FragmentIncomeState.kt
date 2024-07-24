package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentIncomeState {
    class Data(val amount: String, val source: String) : FragmentIncomeState()
    data object Error: FragmentIncomeState()
    data object Finish : FragmentIncomeState()
}