package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Limit

sealed class LimitsState {
    data object NoData : LimitsState()
    data object HasData : LimitsState()
    class LimitsList(val list: List<Limit>) : LimitsState()
    data object NoCategoriesError: LimitsState()
    data object PermitAddLimit: LimitsState()
}
