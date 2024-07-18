package com.elliemoritz.coinbook.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.elliemoritz.coinbook.presentation.states.MainData
import com.elliemoritz.coinbook.presentation.states.MainState
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class MainViewModel @Inject constructor(
    application: Application,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val editBalanceUseCase: EditBalanceUseCase,
    private val getTotalIncomeAmountFromDateUseCase: GetTotalIncomeAmountFromDateUseCase,
    private val getTotalExpensesAmountFromDateUseCase: GetTotalExpensesAmountFromDateUseCase,
    private val getMoneyBoxUseCase: GetMoneyBoxUseCase,
    private val getTotalMoneyBoxAmountFromDateUseCase: GetTotalMoneyBoxAmountFromDateUseCase,
    private val getTotalDebtsAmountUseCase: GetTotalDebtsAmountUseCase,
    private val getLimitsCountUseCase: GetLimitsCountUseCase,
    private val getAlarmsCountUseCase: GetAlarmsCountUseCase
) : AndroidViewModel(application) {

    private val _mainState = MutableLiveData<MainState>()
    val mainState: LiveData<MainState>
        get() = _mainState

    fun setValues() {
        viewModelScope.launch {
            val moneyBox = getMoneyBox()
            val debtsAmount = getDebtsAmount()
            _mainState.value = MainData(
                balance = getBalance().toString(),
                income = getIncome().toString(),
                expenses = getExpenses().toString(),
                hasMoneyBox = moneyBox != null,
                moneyBoxAmount = getMoneyBoxAmount(moneyBox?.started).toString(),
                hasDebts = debtsAmount > NO_OPERATIONS,
                debtsAmount = debtsAmount.toString(),
                hasLimits = hasLimits(),
                hasAlarms = hasAlarms()
            )
        }
    }

    fun editBalance(newAmount: Int) {
        viewModelScope.launch {
            editBalanceUseCase(newAmount)
            setValues()
        }
    }

    private suspend fun getBalance(): Int {
        return getBalanceUseCase()
    }

    private suspend fun getIncome(): Int {
        val beginOfMonth = getBeginOfMonthTimestamp()
        return getTotalIncomeAmountFromDateUseCase(beginOfMonth)
    }

    private suspend fun getExpenses(): Int {
        val beginOfMonth = getBeginOfMonthTimestamp()
        return getTotalExpensesAmountFromDateUseCase(beginOfMonth)
    }

    private suspend fun getMoneyBox(): MoneyBox? {
        return getMoneyBoxUseCase(MoneyBox.MONEY_BOX_ID)
    }

    private suspend fun getMoneyBoxAmount(started: Timestamp?): Int {
        if (started != null) {
            return getTotalMoneyBoxAmountFromDateUseCase(started)
        }
        return NO_OPERATIONS
    }

    private suspend fun getDebtsAmount(): Int {
        return getTotalDebtsAmountUseCase()
    }

    private suspend fun hasLimits(): Boolean {
        val limitsCount = getLimitsCountUseCase()
        return limitsCount > 0
    }

    private suspend fun hasAlarms(): Boolean {
        val alarmsCount = getAlarmsCountUseCase()
        return alarmsCount > 0
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