package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentIncomeState {
    class Data(val amount: String, val source: String) : FragmentIncomeState()
    data object EmptyFields: FragmentIncomeState()
    data object NoChanges: FragmentIncomeState()
    data object IncorrectNumber: FragmentIncomeState()
    data object Finish : FragmentIncomeState()
}