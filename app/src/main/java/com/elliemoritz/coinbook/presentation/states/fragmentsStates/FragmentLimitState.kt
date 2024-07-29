package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentLimitState {
    class Amount(val amount: String) : FragmentLimitState()
    class CategoryPosition(val categoryPosition: Int) : FragmentLimitState()
    class Categories(val categories: List<String>) : FragmentLimitState()
    data object EmptyFields : FragmentLimitState()
    data object NoChanges : FragmentLimitState()
    data object IncorrectNumber : FragmentLimitState()
    data object Finish : FragmentLimitState()
}