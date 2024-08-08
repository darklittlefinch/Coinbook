package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases.AddDebtOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.EditDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetDebtsListUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetTotalDebtsAmountUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.RemoveDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.DebtsState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.getCurrentTimeMillis
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DebtsViewModel @Inject constructor(
    getCurrencyUseCase: GetCurrencyUseCase,
    private val getDebtsListUseCase: GetDebtsListUseCase,
    getTotalDebtsAmountUseCase: GetTotalDebtsAmountUseCase,
    private val editDebtUseCase: EditDebtUseCase,
    private val removeDebtUseCase: RemoveDebtUseCase,
    private val addDebtOperationUseCase: AddDebtOperationUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val currencyFlow = getCurrencyUseCase()
    private val currencyStateFlow = currencyFlow
        .map { DebtsState.Currency(it) }

    private val debtsListFlow = getDebtsListUseCase()
    private val hasDataStateFlow = debtsListFlow
        .map {
            if (it.isEmpty()) {
                DebtsState.NoData
            } else {
                DebtsState.HasData
            }
        }
    private val debtsListStateFlow = debtsListFlow
        .map { DebtsState.DebtsList(it) }

    private val amountStateFlow = getTotalDebtsAmountUseCase(finished = false)
        .map {
            val currency = currencyFlow.first()
            val formattedAmount = formatAmount(it, currency)
            DebtsState.Amount(formattedAmount)
        }

    private val _state = MutableSharedFlow<DebtsState>()

    val state: Flow<DebtsState>
        get() = _state
            .mergeWith(currencyStateFlow)
            .mergeWith(hasDataStateFlow)
            .mergeWith(debtsListStateFlow)
            .mergeWith(amountStateFlow)

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

    fun changeFinishedStatus(debt: Debt) {
        viewModelScope.launch {
            if (debt.finished) {
                setDebtNotFinished(debt)
            } else {
                setDebtFinished(debt)
            }
            val list = getDebtsListUseCase().first()
            _state.emit(DebtsState.DebtsList(list))
        }
    }

    private suspend fun setDebtFinished(debt: Debt) {
        val balance = getBalanceUseCase().first()

        if (balance < debt.amount) {
            _state.emit(DebtsState.NotEnoughMoney)
        } else {
            val newDebt = debt.copy(finished = true)
            editDebtUseCase(newDebt)

            val debtOperation = DebtOperation(
                amount = newDebt.amount,
                type = Type.EXPENSE,
                debtId = debt.id,
                debtCreditor = newDebt.creditor,
                dateTimeMillis = getCurrentTimeMillis()
            )

            addDebtOperationUseCase(debtOperation)
            removeFromBalanceUseCase(newDebt.amount)
        }
    }

    private suspend fun setDebtNotFinished(debt: Debt) {
        val newDebt = debt.copy(finished = false)
        editDebtUseCase(newDebt)

        val debtOperation = DebtOperation(
            amount = debt.amount,
            type = Type.INCOME,
            debtId = debt.id,
            debtCreditor = newDebt.creditor,
            dateTimeMillis = getCurrentTimeMillis()
        )
        addDebtOperationUseCase(debtOperation)

        addToBalanceUseCase(newDebt.amount)
    }
}