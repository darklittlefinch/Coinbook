package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.expensesUseCases.GetExpensesListForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.expensesUseCases.GetTotalExpensesAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.expensesUseCases.RemoveExpenseUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.presentation.states.ExpenseState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseViewModel @Inject constructor(
    getCurrencyUseCase: GetCurrencyUseCase,
    getTotalExpensesAmountForMonthUseCase: GetTotalExpensesAmountForMonthUseCase,
    getExpensesListForMonthUseCase: GetExpensesListForMonthUseCase,
    private val removeExpenseUseCase: RemoveExpenseUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val getCategoriesListUseCase: GetCategoriesListUseCase
) : ViewModel() {

    private val currencyFlow = getCurrencyUseCase()

    private val amountStateFlow = getTotalExpensesAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            ExpenseState.Amount(formatAmount(it, currency))
        }

    private val expensesListFlow = getExpensesListForMonthUseCase()
    private val expensesListStateFlow = expensesListFlow
        .map { ExpenseState.ExpensesList(it) }
    private val hasDataStateFlow = expensesListFlow
        .map {
            if (it.isEmpty()) {
                ExpenseState.NoData
            } else {
                ExpenseState.HasData
            }
        }

    private val _state = MutableSharedFlow<ExpenseState>()

    val state: Flow<ExpenseState>
        get() = _state
            .mergeWith(amountStateFlow)
            .mergeWith(expensesListStateFlow)
            .mergeWith(hasDataStateFlow)

    fun removeExpense(expense: Expense) {
        viewModelScope.launch {
            removeExpenseUseCase(expense)
            addToBalanceUseCase(expense.amount)
        }
    }

    fun checkCategories() {
        viewModelScope.launch {
            val categories = getCategoriesListUseCase().first()
            if (categories.isEmpty()) {
                _state.emit(ExpenseState.NoCategoriesError)
            } else {
                _state.emit(ExpenseState.PermitAddExpense)
            }
        }
    }
}
