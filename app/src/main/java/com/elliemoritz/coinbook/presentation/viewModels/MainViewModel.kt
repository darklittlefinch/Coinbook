package com.elliemoritz.coinbook.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.useCases.alarmsUseCases.GetAlarmsListUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetDebtsListUseCase
import com.elliemoritz.coinbook.domain.useCases.limitsUseCases.GetLimitsListUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetExpensesListFromDateUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetIncomeListFromDateUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class MainViewModel @Inject constructor(
    application: Application,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val editBalanceUseCase: EditBalanceUseCase,
    private val getIncomeListFromDateUseCase: GetIncomeListFromDateUseCase,
    private val getExpensesListFromDateUseCase: GetExpensesListFromDateUseCase,
    private val getMoneyBoxUseCase: GetMoneyBoxUseCase,
    private val getDebtsListUseCase: GetDebtsListUseCase,
    private val getLimitsListUserCase: GetLimitsListUseCase,
    private val getAlarmsListUseCase: GetAlarmsListUseCase
) : AndroidViewModel(application) {

    private val _balanceAmount = MutableLiveData<String>()
    val balanceAmount: LiveData<String>
        get() = _balanceAmount

    private val _incomeAmount = MutableLiveData<String>()
    val incomeAmount: LiveData<String>
        get() = _incomeAmount

    private val _expensesAmount = MutableLiveData<String>()
    val expensesAmount: LiveData<String>
        get() = _expensesAmount

    private val _hasMoneyBox = MutableLiveData<Boolean>()
    val hasMoneyBox: LiveData<Boolean>
        get() = _hasMoneyBox

    private val _moneyBoxAmount = MutableLiveData<String>()
    val moneyBoxAmount: LiveData<String>
        get() = _moneyBoxAmount

    private val _hasDebts = MutableLiveData<Boolean>()
    val hasDebts: LiveData<Boolean>
        get() = _hasDebts

    private val _debtsAmount = MutableLiveData<String>()
    val debtsAmount: LiveData<String>
        get() = _debtsAmount

    private val _hasLimits = MutableLiveData<Boolean>()
    val hasLimits: LiveData<Boolean>
        get() = _hasLimits

    private val _hasAlarms = MutableLiveData<Boolean>()
    val hasAlarms: LiveData<Boolean>
        get() = _hasAlarms

    fun getBalance() {
        viewModelScope.launch {
            _balanceAmount.value = getBalanceUseCase.getBalance().toString()
        }
    }

    fun getIncome() {
        val beginOfMonthDate = getBeginOfMonthTimestamp()
        val incomeList = getIncomeListFromDateUseCase.getIncomeListFromDate(beginOfMonthDate).value
        val sum = incomeList?.sumOf { it.incAmount } ?: 0
        _incomeAmount.value = sum.toString()
    }

    fun getExpenses() {
        val beginOfMonthDate = getBeginOfMonthTimestamp()
        val expensesList = getExpensesListFromDateUseCase
            .getOperationsListFromDate(beginOfMonthDate).value
        val sum = expensesList?.sumOf { it.expAmount } ?: 0
        _expensesAmount.value = sum.toString()
    }

    fun getMoneyBoxTotalAmount() {
        viewModelScope.launch {
            val moneyBox = getMoneyBoxUseCase.getMoneyBox(MoneyBox.MONEY_BOX_ID)
            var amount = 0

            if (moneyBox != null) {
                amount = moneyBox.amount
            }

            if (amount == 0) {
                _hasMoneyBox.value = false
            } else {
                _hasMoneyBox.value = true
            }
            _moneyBoxAmount.value = amount.toString()
        }
    }

    fun getDebtsTotalAmount() {
        val debts = getDebtsListUseCase.getDebtsList().value
        val sum = debts?.sumOf { it.amount } ?: 0
        _hasDebts.value = (sum != 0)
        _debtsAmount.value = sum.toString()
    }

    fun checkLimitsActive() {
        val limits = getLimitsListUserCase.getLimitsList().value
        _hasLimits.value = !limits.isNullOrEmpty()
    }

    fun checkAlarmsActive() {
        val alarms = getAlarmsListUseCase.getAlarmsList().value
        _hasAlarms.value = !alarms.isNullOrEmpty()
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

    fun editBalance(newAmount: Int) {
        viewModelScope.launch {
            editBalanceUseCase.editBalance(newAmount)
            _balanceAmount.value = getBalanceUseCase.getBalance().toString()
        }
    }

    companion object {
        private const val FIRST_DAY_OF_MONTH = 1
        private const val FIRST_HOUR_OF_MONTH = 0
        private const val FIRST_MINUTE_OF_MONTH = 0
    }
}