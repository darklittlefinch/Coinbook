package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Debt

sealed class DebtsState

data object NoDebts : DebtsState()

class DebtsData(
    totalAmount: Int,
    debts: List<Debt>,
    nextRepayment: String
) : DebtsState()

class DebtsDataExceeded(
    totalAmount: Int,
    debts: List<Debt>
) : DebtsState()
