package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.useCases.incomeUseCases.GetIncomeListForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.incomeUseCases.GetTotalIncomeAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.incomeUseCases.RemoveIncomeUseCase
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
    private val removeOperationUseCase: RemoveIncomeUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val currencyFlow = getCurrencyUseCase()

    private val amountStateFlow = getTotalIncomeAmountForMonthUseCase()
        .map {
            val currency = currencyFlow.first()
            IncomeState.Amount(formatAmount(it, currency))
        }

    private val incomeListFlow = getIncomeListForMonthUseCase()
    private val incomeListStateFlow = incomeListFlow
        .map { IncomeState.IncomeList(it) }
    private val hasDataStateFlow = incomeListFlow
        .map {
            if (it.isEmpty()) {
                IncomeState.NoData
            } else {
                IncomeState.HasData
            }
        }

    private val _state = MutableSharedFlow<IncomeState>()

    val state: Flow<IncomeState>
        get() = _state
            .mergeWith(amountStateFlow)
            .mergeWith(incomeListStateFlow)
            .mergeWith(hasDataStateFlow)

    fun removeIncome(income: Income) {
        viewModelScope.launch {
            removeOperationUseCase(income)
            removeFromBalanceUseCase(income.amount)
        }
    }
}