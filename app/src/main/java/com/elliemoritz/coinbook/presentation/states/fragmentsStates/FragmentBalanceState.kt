package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentBalanceState {
    class Data(val amount: String) : FragmentBalanceState()
    data object Error: FragmentBalanceState()
    data object Finish : FragmentBalanceState()
}