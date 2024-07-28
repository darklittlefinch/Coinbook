package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentMoneyBoxOperationState {
    class Data(val amount: String) : FragmentMoneyBoxOperationState()
    data object EmptyFields: FragmentMoneyBoxOperationState()
    data object IncorrectNumber: FragmentMoneyBoxOperationState()
    data object Finish : FragmentMoneyBoxOperationState()
}