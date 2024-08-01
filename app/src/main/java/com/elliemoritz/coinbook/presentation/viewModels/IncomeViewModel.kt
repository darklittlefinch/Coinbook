package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetIncomeListForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetTotalIncomeAmountForMonthUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.presentation.states.IncomeState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IncomeViewModel @Inject constructor(
    getCurrencyUseCase: GetCurrencyUseCase,
    getTotalIncomeAmountForMonthUseCase: GetTotalIncomeAmountForMonthUseCase,
    getIncomeListForMonthUseCase: GetIncomeListForMonthUseCase,
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
                IncomeState.NoData
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
}