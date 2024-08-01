package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.AddOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.EditOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetIncomeUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentIncomeState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
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
            dataFlow.emit(data)
        }
    }

    fun createIncome(amountString: String, source: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(amountString, source)
                checkIncorrectNumbers(amountString)

                val amount = amountString.toInt()
                val income = Income(getCurrentTimestamp(), amount, source)
                addOperationUseCase(income)
                addToBalanceUseCase(amount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editIncome(newAmountString: String, newSource: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newAmountString, newSource)
                checkIncorrectNumbers(newAmountString)

                val oldData = dataFlow.first()
                val newAmount = newAmountString.toInt()

                checkNoChanges(
                    listOf(newAmount, newSource),
                    listOf(oldData.amount, oldData.incSource)
                )

                val income = Income(oldData.date, newAmount, newSource, oldData.id)
                editOperationUseCase(income)

                val oldAmount = oldData.amount
                editBalance(oldAmount, newAmount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: NoChangesException) {
                setNoChangesState()
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

    private suspend fun setNoChangesState() {
        _state.emit(FragmentIncomeState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentIncomeState.IncorrectNumber)
    }
}