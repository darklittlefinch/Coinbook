package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.operations.Income

sealed class IncomeState

class NoIncomeData(
    totalAmount: Int = 0
) : IncomeState()

class IncomeData(
    totalAmount: Int,
    incomeHistory: List<Income>
) : IncomeState()
