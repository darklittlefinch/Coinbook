package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentExpenseState {
    class Categories(val categories: List<String>) : FragmentExpenseState()
    class Data(val amount: String, val categoryName: String) : FragmentExpenseState()
    data object Error : FragmentExpenseState()
    data object Finish : FragmentExpenseState()
}