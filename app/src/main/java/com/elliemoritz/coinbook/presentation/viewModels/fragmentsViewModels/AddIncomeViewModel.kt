package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.AddOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.EditOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetIncomeUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentIncomeState
import com.elliemoritz.coinbook.presentation.util.getCurrentTimestamp
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

class AddIncomeViewModel @Inject constructor(
    private val getIncomeUseCase: GetIncomeUseCase,
    private val addOperationUseCase: AddOperationUseCase,
    private val editOperationUseCase: EditOperationUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val dataFlow = MutableSharedFlow<Income>()

    private val dataStateFlow = dataFlow
        .map {
            FragmentIncomeState.Data(
                it.incAmount.toString(),
                it.incSource
            )
        }

    private val _state = MutableSharedFlow<FragmentIncomeState>()

    val state: Flow<FragmentIncomeState>
        get() = _state
            .mergeWith(dataStateFlow)

    fun setData(id: Int) {
        viewModelScope.launch {
            val data = getIncomeUseCase(id).first()
            this@AddIncomeViewModel.dataFlow.emit(data)
        }
    }

    fun createIncome(amountString: String, source: String) {
        viewModelScope.launch {

            if (amountString.isEmpty() || source.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val amount = amountString.toInt()
                val income = Income(getCurrentTimestamp(), amount, source)
                addOperationUseCase(income)
                addToBalanceUseCase(amount)
                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editIncome(newAmountString: String, source: String) {
        viewModelScope.launch {

            if (newAmountString.isEmpty() || source.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val id = dataFlow.first().id
                val newAmount = newAmountString.toInt()
                val income = Income(getCurrentTimestamp(), newAmount, source, id)
                editOperationUseCase(income)

                val oldAmount = dataFlow.first().amount
                editBalance(oldAmount, newAmount)

                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    private suspend fun editBalance(oldAmount: Int, newAmount: Int) {
        val difference = abs(newAmount - oldAmount)
        if (newAmount > oldAmount) {
            addToBalanceUseCase(difference)
        } else if (newAmount < oldAmount) {
            removeFromBalanceUseCase(difference)
        }
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentIncomeState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentIncomeState.EmptyFields)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentIncomeState.IncorrectNumber)
    }
}