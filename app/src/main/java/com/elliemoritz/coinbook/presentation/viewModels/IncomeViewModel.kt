package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetIncomeListForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalIncomeAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.RemoveOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.IncomeState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class IncomeViewModel @Inject constructor(
    getCurrencyUseCase: GetCurrencyUseCase,
    getTotalIncomeAmountForMonthUseCase: GetTotalIncomeAmountForMonthUseCase,
    getIncomeListForMonthUseCase: GetIncomeListForMonthUseCase,
    private val removeOperationUseCase: RemoveOperationUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val currencyFlow = getCurrencyUseCase()
    private val currencyStateFlow = currencyFlow
        .map { IncomeState.Currency(it) }

    private val amountStateFlow = getTotalIncomeAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            IncomeState.Amount(formatAmount(it, currency))
        }

    private val incomeListFlow = getIncomeListForMonthUseCase()
        .map {
            if (it.isEmpty()) {
                val currency = currencyFlow.first()
                val formattedAmount = formatAmount(NO_DATA_VALUE, currency)
                IncomeState.NoData(formattedAmount)
            } else {
                IncomeState.IncomeList(it)
            }
        }

    private val _state = MutableSharedFlow<IncomeState>()

    val state: Flow<IncomeState>
        get() = _state
            .mergeWith(amountStateFlow)
            .mergeWith(currencyStateFlow)
            .mergeWith(incomeListFlow)

    fun removeIncome(income: Income) {
        viewModelScope.launch {
            removeOperationUseCase(income)
            removeFromBalanceUseCase(income.amount)
        }
    }

    companion object {
        private const val NO_DATA_VALUE = 0
    }
}