package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentExpenseState {
    class Categories(val categories: List<String>) : FragmentExpenseState()
    class Amount(val amount: String) : FragmentExpenseState()
    class CategoryPosition(val position: Int) : FragmentExpenseState()
    data object EmptyFields : FragmentExpenseState()
    data object NoChanges : FragmentExpenseState()
    data object IncorrectNumber : FragmentExpenseState()
    data object Finish : FragmentExpenseState()
}