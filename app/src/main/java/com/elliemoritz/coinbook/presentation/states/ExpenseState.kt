package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Category

sealed class ExpenseState

class NoExpensesData(
    totalAmount: Int = 0
) : ExpenseState()

class ExpensesData(
    totalAmount: Int,
    categories: List<Category>
) : ExpenseState()

class ExpensesDataLimit(
    totalAmount: Int,
    limitRemains: Int,
    categories: List<Category>
) : ExpenseState()

class ExpensesDataLimitExceeded(
    totalAmount: Int,
    limitExceeded: Int,
    categories: List<Category>
) : ExpenseState()
