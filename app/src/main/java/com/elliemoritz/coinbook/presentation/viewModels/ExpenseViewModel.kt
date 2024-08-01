package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetExpensesListForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalExpensesAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.RemoveOperationUseCase
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
    private val removeOperationUseCase: RemoveOperationUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val getCategoriesListUseCase: GetCategoriesListUseCase
) : ViewModel() {

    private val currencyFlow = getCurrencyUseCase()
    private val currencyStateFlow = currencyFlow
        .map { ExpenseState.Currency(it) }

    private val amountStateFlow = getTotalExpensesAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            ExpenseState.Amount(formatAmount(it, currency))
        }

    private val expensesListStateFlow = getExpensesListForMonthUseCase()
        .map {
            if (it.isEmpty()) {
                val currency = currencyFlow.first()
                val formattedAmount = formatAmount(NO_DATA_VALUE, currency)
                ExpenseState.NoData(formattedAmount)
            } else {
                ExpenseState.ExpensesList(it)
            }
        }

    private val _state = MutableSharedFlow<ExpenseState>()

    val state: Flow<ExpenseState>
        get() = _state
            .mergeWith(amountStateFlow)
            .mergeWith(currencyStateFlow)
            .mergeWith(expensesListStateFlow)

    fun removeExpense(expense: Expense) {
        viewModelScope.launch {
            removeOperationUseCase(expense)
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

    companion object {
        private const val NO_DATA_VALUE = 0
    }
}
