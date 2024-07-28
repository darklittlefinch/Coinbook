package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentDebtState {
    class Data(val amount: String, val creditor: String) : FragmentDebtState()
    data object EmptyFields: FragmentDebtState()
    data object IncorrectNumber: FragmentDebtState()
    data object Finish : FragmentDebtState()
}