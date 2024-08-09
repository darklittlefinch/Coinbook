package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.operations.Expense

sealed class ExpenseState {
    data object NoData : ExpenseState()
    data object HasData : ExpenseState()
    class Amount(val amount: String) : ExpenseState()
    class ExpensesList(val list: List<Expense>) : ExpenseState()
    data object NoCategoriesError : ExpenseState()
    data object PermitAddExpense : ExpenseState()
}
