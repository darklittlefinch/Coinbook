package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.AddDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.EditDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetDebtsListUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.RemoveDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.DebtsState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DebtsViewModel @Inject constructor(
    getCurrencyUseCase: GetCurrencyUseCase,
    getDebtsListUseCase: GetDebtsListUseCase,
    private val editDebtUseCase: EditDebtUseCase,
    private val removeDebtUseCase: RemoveDebtUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val currencyFlow = getCurrencyUseCase()
    private val currencyStateFlow = currencyFlow
        .map { DebtsState.Currency(it) }

    private val debtsListFlow = getDebtsListUseCase()

    private val amountStateFlow = debtsListFlow
        .map {
            val currency = currencyFlow.first()

            if (it.isEmpty()) {
                DebtsState.NoData(formatAmount(NO_DATA_VALUE, currency))
            } else {
                val totalAmount = it.sumOf { debt -> debt.amount }
                DebtsState.Amount(formatAmount(totalAmount, currency))
            }
        }

    private val debtsListStateFlow = debtsListFlow
        .map { DebtsState.DebtsList(it) }

    private val _state = MutableSharedFlow<DebtsState>()

    val state: Flow<DebtsState>
        get() = _state
            .mergeWith(amountStateFlow)
            .mergeWith(currencyStateFlow)
            .mergeWith(debtsListStateFlow)

    fun removeDebt(debt: Debt) {
        viewModelScope.launch {
            val balance = getBalanceUseCase().first()

            if (balance < debt.amount) {
                _state.emit(DebtsState.NotEnoughMoney)
            } else {
                removeFromBalanceUseCase(debt.amount)
                removeDebtUseCase(debt)
            }
        }
    }

    fun setDebtFinished(debt: Debt) {
        viewModelScope.launch {
            val balance = getBalanceUseCase().first()

            if (balance < debt.amount) {
                _state.emit(DebtsState.NotEnoughMoney)
            } else {
                debt.finished = true
                editDebtUseCase(debt)
                removeFromBalanceUseCase(debt.amount)
            }
        }
    }

    companion object {
        private const val NO_DATA_VALUE = 0
    }
}