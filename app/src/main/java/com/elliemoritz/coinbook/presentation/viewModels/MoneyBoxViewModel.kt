package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases.GetMoneyBoxOperationsListUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases.RemoveMoneyBoxOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.MoneyBoxState
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoneyBoxViewModel @Inject constructor(
    getCurrencyUseCase: GetCurrencyUseCase,
    getMoneyBoxUseCase: GetMoneyBoxUseCase,
    getMoneyBoxOperationsListUseCase: GetMoneyBoxOperationsListUseCase,
    private val removeMoneyBoxOperationUseCase: RemoveMoneyBoxOperationUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val currencyFlow = getCurrencyUseCase()
    private val moneyBoxFlow = getMoneyBoxUseCase()
    private val goalStateFlow = moneyBoxFlow
        .map {
            if (it == null) {
                MoneyBoxState.NoMoneyBox
            } else {
                val currency = currencyFlow.first()
                MoneyBoxState.Goal(
                    formatAmount(it.goalAmount, currency),
                    it.goal
                )
            }
        }

    private val totalAmountStateFlow = moneyBoxFlow
        .map {
            val totalAmount = it?.totalAmount ?: NO_DATA_VALUE
            val currency = currencyFlow.first()
            val formattedAmount = formatAmount(totalAmount, currency)
            MoneyBoxState.TotalAmount(formattedAmount)
        }

    private val operationsListStateFlow = getMoneyBoxOperationsListUseCase()
        .map {
            val startedMillis = moneyBoxFlow.first()?.startedMillis ?: NO_MILLIS
            val operationsList = if (startedMillis == NO_MILLIS) {
                emptyList()
            } else {
                it.filter { operation ->
                    operation.dateTimeMillis >= startedMillis
                }
            }
            MoneyBoxState.OperationsList(operationsList)
        }

    private val _state = MutableSharedFlow<MoneyBoxState>()

    val state: Flow<MoneyBoxState>
        get() = _state
            .mergeWith(goalStateFlow)
            .mergeWith(totalAmountStateFlow)
            .mergeWith(operationsListStateFlow)

    fun removeMoneyBoxOperation(operation: MoneyBoxOperation) {
        viewModelScope.launch {
            removeMoneyBoxOperationUseCase(operation)
            handleBalanceEditing(operation)
        }
    }

    private suspend fun handleBalanceEditing(operation: MoneyBoxOperation) {
        when (operation.type) {
            Type.INCOME -> removeFromBalanceUseCase(operation.amount)
            Type.EXPENSE -> addToBalanceUseCase(operation.amount)
        }
    }

    companion object {
        private const val NO_DATA_VALUE = 0
        private const val NO_MILLIS = -1L
    }
}
