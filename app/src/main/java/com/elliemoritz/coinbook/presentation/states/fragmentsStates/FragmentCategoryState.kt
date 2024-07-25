package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentCategoryState {
    class Name(val name: String) : FragmentCategoryState()
    class Limit(val limit: String) : FragmentCategoryState()
    data object Error : FragmentCategoryState()
    data object Finish : FragmentCategoryState()
}