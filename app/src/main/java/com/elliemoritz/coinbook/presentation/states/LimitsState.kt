package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Limit

sealed class LimitsState

data object NoLimits : LimitsState()

class LimitsData(
    limits: List<Limit>,
    totalLimitRemains: Int = 0
) : LimitsState()

class LimitsDataExceeded(
    limits: List<Limit>,
    totalLimitExceeded: Int
) : LimitsState()
