package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentCategoryState {
    class Name(val name: String) : FragmentCategoryState()
    class Limit(val limit: String) : FragmentCategoryState()
    data object EmptyFields : FragmentCategoryState()
    data object IncorrectNumber : FragmentCategoryState()
    data object Finish : FragmentCategoryState()
}