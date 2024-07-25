package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentMoneyBoxState {
    class Data(val goalAmount: String, val goal: String) : FragmentMoneyBoxState()
    data object Error: FragmentMoneyBoxState()
    data object Finish : FragmentMoneyBoxState()
}