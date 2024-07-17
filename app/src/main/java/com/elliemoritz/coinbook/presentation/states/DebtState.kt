package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Debt

sealed class DebtState

class NoDebtsData(
    totalAmount: Int = 0
) : DebtState()

class DebtsData(
    totalAmount: Int,
    debts: List<Debt>,
    nextRepayment: String
) : DebtState()

class DebtsDataExceeded(
    totalAmount: Int,
    debts: List<Debt>
) : DebtState()
