package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentBalanceState {
    class Data(val amount: String) : FragmentBalanceState()
    data object EmptyFields: FragmentBalanceState()
    data object NoChanges: FragmentBalanceState()
    data object IncorrectNumber: FragmentBalanceState()
    data object Finish : FragmentBalanceState()
}