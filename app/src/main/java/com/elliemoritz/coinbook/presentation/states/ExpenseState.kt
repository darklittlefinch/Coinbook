package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.operations.Expense

sealed class ExpenseState {
    class NoData(val amount: String) : ExpenseState()
    class Amount(val amount: String) : ExpenseState()
    class ExpensesList(val list: List<Expense>) : ExpenseState()
    class Currency(val currency: String) : ExpenseState()
    data object NoCategoriesError : ExpenseState()
    data object PermitAddExpense : ExpenseState()
}
