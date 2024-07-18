package com.elliemoritz.coinbook.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.useCases.alarmsUseCases.GetAlarmsListUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetTotalDebtsAmountUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitsListUseCase
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
    private val getLimitsListUserCase: GetLimitsListUseCase,
    private val getAlarmsListUseCase: GetAlarmsListUseCase
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
                hasLimits = checkLimitsActive(),
                hasAlarms = checkAlarmsActive()
            )
        }
    }

    fun editBalance(newAmount: Int) {
        viewModelScope.launch {
            editBalanceUseCase.editBalance(newAmount)
            setValues()
        }
    }

    private suspend fun getBalance(): Int {
        return getBalanceUseCase.getBalance()
    }

    private suspend fun getIncome(): Int {
        val beginOfMonth = getBeginOfMonthTimestamp()
        return getTotalIncomeAmountFromDateUseCase.getTotalIncomeAmountFromDate(beginOfMonth)
    }

    private suspend fun getExpenses(): Int {
        val beginOfMonth = getBeginOfMonthTimestamp()
        return getTotalExpensesAmountFromDateUseCase.getTotalExpensesAmountFromDate(beginOfMonth)
    }

    private suspend fun getMoneyBox(): MoneyBox? {
        return getMoneyBoxUseCase.getMoneyBox(MoneyBox.MONEY_BOX_ID)
    }

    private suspend fun getMoneyBoxAmount(started: Timestamp?): Int {
        if (started != null) {
            return getTotalMoneyBoxAmountFromDateUseCase.getTotalMoneyBoxAmountFromDate(started)
        }
        return NO_OPERATIONS
    }

    private suspend fun getDebtsAmount(): Int {
        return getTotalDebtsAmountUseCase.getTotalDebtsAmount()
    }

    private fun checkLimitsActive(): Boolean {
        val limits = getLimitsListUserCase.getLimitsList().value
        return !limits.isNullOrEmpty()
    }

    private fun checkAlarmsActive(): Boolean {
        val alarms = getAlarmsListUseCase.getAlarmsList().value
        return !alarms.isNullOrEmpty()
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