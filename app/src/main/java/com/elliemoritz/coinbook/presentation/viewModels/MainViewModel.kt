package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.useCases.alarmsUseCases.GetAlarmsCountUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetTotalDebtsAmountUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitsCountUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalExpensesAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalIncomeAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.presentation.states.MainState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getBalanceUseCase: GetBalanceUseCase,
    getCurrencyUseCase: GetCurrencyUseCase,
    getTotalIncomeAmountForMonthUseCase: GetTotalIncomeAmountForMonthUseCase,
    getTotalExpensesAmountForMonthUseCase: GetTotalExpensesAmountForMonthUseCase,
    getMoneyBoxUseCase: GetMoneyBoxUseCase,
    getTotalDebtsAmountUseCase: GetTotalDebtsAmountUseCase,
    getLimitsCountUseCase: GetLimitsCountUseCase,
    getAlarmsCountUseCase: GetAlarmsCountUseCase
) : ViewModel() {

    companion object {
        private const val NO_DATA = 0
    }

    private val currencyFlow = getCurrencyUseCase()

    private val balanceFlow = getBalanceUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Balance(formatAmount(it, currency))
        }

    private val incomeFlow = getTotalIncomeAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Income(formatAmount(it, currency)) }

    private val expensesFlow = getTotalExpensesAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Expenses(formatAmount(it, currency))
        }

    private val moneyBoxFlow = getMoneyBoxUseCase(MoneyBox.MONEY_BOX_ID)
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

    private val debtsAmountFlow = getTotalDebtsAmountUseCase()
        .map {
            val currency = currencyFlow.first()
            MainState.Debts(formatAmount(it, currency), it > NO_DATA)
        }

    private val limitsFlow = getLimitsCountUseCase()
        .map { MainState.Limits(it > NO_DATA) }

    private val alarmsFlow = getAlarmsCountUseCase()
        .map { MainState.Alarms(it > NO_DATA) }

    private val _state = MutableSharedFlow<MainState>()
        .onStart { emit(MainState.Loading) }
        .mergeWith(balanceFlow)
        .mergeWith(incomeFlow)
        .mergeWith(expensesFlow)
        .mergeWith(moneyBoxFlow)
        .mergeWith(debtsAmountFlow)
        .mergeWith(limitsFlow)
        .mergeWith(alarmsFlow)

    private fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
        return merge(this, another)
    }

    val state: Flow<MainState>
        get() = _state
}
