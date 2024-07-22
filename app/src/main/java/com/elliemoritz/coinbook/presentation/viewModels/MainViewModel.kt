package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.useCases.alarmsUseCases.GetAlarmsCountUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetTotalDebtsAmountUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitsCountUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalExpensesAmountFromDateUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalIncomeAmountFromDateUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalMoneyBoxAmountFromDateUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.presentation.states.MainState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val editBalanceUseCase: EditBalanceUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getTotalIncomeAmountFromDateUseCase: GetTotalIncomeAmountFromDateUseCase,
    private val getTotalExpensesAmountFromDateUseCase: GetTotalExpensesAmountFromDateUseCase,
    private val getMoneyBoxUseCase: GetMoneyBoxUseCase,
    private val getTotalMoneyBoxAmountFromDateUseCase: GetTotalMoneyBoxAmountFromDateUseCase,
    private val getTotalDebtsAmountUseCase: GetTotalDebtsAmountUseCase,
    private val getLimitsCountUseCase: GetLimitsCountUseCase,
    private val getAlarmsCountUseCase: GetAlarmsCountUseCase
) : ViewModel() {

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState>
        get() = _state

    fun setValues() {
        viewModelScope.launch {
            _state.value = MainState.StartLoading
            getMainDataStates()
            _state.value = MainState.EndLoading
        }
    }

    fun editBalance(newAmount: Int) {
        viewModelScope.launch {
            editBalanceUseCase(newAmount)
            setValues()
        }
    }

    private suspend fun getMainDataStates() {
        val currency = getCurrencyUseCase()
        val beginOfMonth = getBeginOfMonthTimestamp()
        setBalanceAmount(currency)
        setIncomeAmount(currency, beginOfMonth)
        setExpensesAmount(currency, beginOfMonth)
        setMoneyBoxAmount(currency)
        setDebtsAmount(currency)
        setLimitsPresent()
        setAlarmsPresent()
    }

    private suspend fun setBalanceAmount(currency: String) {
        val balanceAmount = getBalanceUseCase()
        val formattedBalanceAmount = formatAmount(balanceAmount, currency)
        _state.value = MainState.Balance(formattedBalanceAmount)
    }

    private suspend fun setIncomeAmount(currency: String, beginOfMonth: Timestamp) {
        val incomeAmount = getTotalIncomeAmountFromDateUseCase(beginOfMonth)
        val formattedIncomeAmount = formatAmount(incomeAmount, currency)
        _state.value = MainState.Income(formattedIncomeAmount)
    }

    private suspend fun setExpensesAmount(currency: String, beginOfMonth: Timestamp) {
        val expensesAmount = getTotalExpensesAmountFromDateUseCase(beginOfMonth)
        val formattedExpensesAmount = formatAmount(expensesAmount, currency)
        _state.value = MainState.Expenses(formattedExpensesAmount)
    }

    private suspend fun setMoneyBoxAmount(currency: String) {
        val moneyBox = getMoneyBoxUseCase(MoneyBox.MONEY_BOX_ID)

        val moneyBoxAmount: Int
        val wasMoneyBoxStarted: Boolean

        if (moneyBox != null) {
            moneyBoxAmount = getTotalMoneyBoxAmountFromDateUseCase(moneyBox.started)
            wasMoneyBoxStarted = true
        } else {
            moneyBoxAmount = NO_OPERATIONS
            wasMoneyBoxStarted = false
        }

        val formattedMoneyBoxAmount = formatAmount(moneyBoxAmount, currency)
        _state.value = MainState.MoneyBox(
            formattedMoneyBoxAmount,
            wasMoneyBoxStarted
        )
    }

    private suspend fun setDebtsAmount(currency: String) {
        val debtsAmount = getTotalDebtsAmountUseCase()
        val formattedDebtsAmount = formatAmount(debtsAmount, currency)
        val userHasDebts = debtsAmount != 0
        _state.value = MainState.Debts(formattedDebtsAmount, userHasDebts)
    }

    private suspend fun setLimitsPresent() {
        val limitsCount = getLimitsCountUseCase()
        val userHasLimits = limitsCount > 0
        _state.value = MainState.Limits(userHasLimits)
    }

    private suspend fun setAlarmsPresent() {
        val alarmsCount = getAlarmsCountUseCase()
        val userHasAlarms = alarmsCount > 0
        _state.value = MainState.Alarms(userHasAlarms)
    }

    private fun getBeginOfMonthTimestamp(): Timestamp {
        val currentTime = LocalDateTime.now()
        val month = currentTime.monthValue
        val year = currentTime.year
        val beginOfMonthMillis = LocalDateTime.of(
            year,
            month,
            FIRST_DAY_OF_MONTH,
            FIRST_HOUR_OF_MONTH,
            FIRST_MINUTE_OF_MONTH
        ).toInstant(ZoneOffset.UTC).toEpochMilli()
        return Timestamp(beginOfMonthMillis)
    }

    companion object {
        private const val FIRST_DAY_OF_MONTH = 1
        private const val FIRST_HOUR_OF_MONTH = 0
        private const val FIRST_MINUTE_OF_MONTH = 0

        private const val NO_OPERATIONS = 0
    }
}