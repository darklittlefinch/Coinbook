package com.elliemoritz.coinbook.presentation.states.fragmentsStates

sealed class FragmentCategoryState {
    class NameData(val name: String) : FragmentCategoryState()
    class LimitData(val limit: String) : FragmentCategoryState()
    data object Error : FragmentCategoryState()
    data object Finish : FragmentCategoryState()
}