package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.useCases.alarmsUseCases.GetAlarmsCountUseCase
import com.elliemoritz.coinbook.domain.useCases.categoriesUseCases.GetCategoriesListUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetTotalDebtsAmountUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitsCountUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalExpensesAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalIncomeAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.presentation.states.MainState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getBalanceUseCase: GetBalanceUseCase,
    getCurrencyUseCase: GetCurrencyUseCase,
    getTotalIncomeAmountForMonthUseCase: GetTotalIncomeAmountForMonthUseCase,
    getTotalExpensesAmountForMonthUseCase: GetTotalExpensesAmountForMonthUseCase,
    getMoneyBoxUseCase: GetMoneyBoxUseCase,
    getTotalDebtsAmountUseCase: GetTotalDebtsAmountUseCase,
    getLimitsCountUseCase: GetLimitsCountUseCase,
    getAlarmsCountUseCase: GetAlarmsCountUseCase,
    private val getCategoriesListUseCase: GetCategoriesListUseCase
) : ViewModel() {

    companion object {
        private const val NO_DATA = 0
    }

    private val currencyFlow = getCurrencyUseCase()

    private val balanceStateFlow = getBalanceUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Balance(formatAmount(it, currency))
        }

    private val incomeStateFlow = getTotalIncomeAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Income(formatAmount(it, currency))
        }

    private val expensesStateFlow = getTotalExpensesAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Expenses(formatAmount(it, currency))
        }

    private val moneyBoxStateFlow = getMoneyBoxUseCase()
        .map {
            val moneyBoxWasStarted: Boolean
            val moneyBoxAmount: Int

            if (it != null) {
                moneyBoxWasStarted = true
                moneyBoxAmount = it.totalAmount
            } else {
                moneyBoxWasStarted = false
                moneyBoxAmount = NO_DATA
            }

            val currency = currencyFlow.first()
            MainState.MoneyBox(
                formatAmount(moneyBoxAmount, currency),
                moneyBoxWasStarted
            )
        }

    private val debtsAmountStateFlow = getTotalDebtsAmountUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Debts(formatAmount(it, currency), it > NO_DATA)
        }

    private val limitsStateFlow = getLimitsCountUseCase()
        .map { MainState.Limits(it > NO_DATA) }

    private val alarmsStateFlow = getAlarmsCountUseCase()
        .map { MainState.Alarms(it > NO_DATA) }

    private val categoriesStateFlow = MutableSharedFlow<MainState>()

    private val _state = MutableSharedFlow<MainState>()
        .onStart { emit(MainState.Loading) }

    val state: Flow<MainState>
        get() = _state
            .mergeWith(balanceStateFlow)
            .mergeWith(incomeStateFlow)
            .mergeWith(expensesStateFlow)
            .mergeWith(moneyBoxStateFlow)
            .mergeWith(debtsAmountStateFlow)
            .mergeWith(limitsStateFlow)
            .mergeWith(alarmsStateFlow)
            .mergeWith(categoriesStateFlow)

    fun checkCategories() {
        viewModelScope.launch {
            val categories = getCategoriesListUseCase().first()
            if (categories.isEmpty()) {
                categoriesStateFlow.emit(MainState.NoCategoriesError)
            } else {
                categoriesStateFlow.emit(MainState.PermitAddExpense)
            }
        }
    }
}
