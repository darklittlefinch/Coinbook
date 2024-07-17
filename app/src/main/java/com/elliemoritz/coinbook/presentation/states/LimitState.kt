package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Limit

sealed class LimitState

data object NoLimitsData : LimitState()

class LimitData(
    limits: List<Limit>,
    totalLimitRemains: Int = 0
) : LimitState()

class LimitDataExceeded(
    limits: List<Limit>,
    totalLimitExceeded: Int
) : LimitState()
