package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentMoneyBoxState {
    class Data(val goalAmount: String, val goal: String) : FragmentMoneyBoxState()
    data object EmptyFields: FragmentMoneyBoxState()
    data object NoChanges: FragmentMoneyBoxState()
    data object IncorrectNumber: FragmentMoneyBoxState()
    data object Finish : FragmentMoneyBoxState()
}