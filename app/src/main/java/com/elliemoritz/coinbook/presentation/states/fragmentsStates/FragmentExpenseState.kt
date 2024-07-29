package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentExpenseState {
    class Categories(val categories: List<String>) : FragmentExpenseState()
    class Data(val amount: String, val categoryName: String) : FragmentExpenseState()
    data object EmptyFields : FragmentExpenseState()
    data object NoChanges : FragmentExpenseState()
    data object IncorrectNumber : FragmentExpenseState()
    data object Finish : FragmentExpenseState()
}