package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.AddToMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.RemoveFromMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.AddOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.EditOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetMoneyBoxOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentMoneyBoxOperationState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
import com.elliemoritz.coinbook.presentation.util.getCurrentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

class AddMoneyBoxOperationViewModel @Inject constructor(
    private val getMoneyBoxOperationUseCase: GetMoneyBoxOperationUseCase,
    private val addOperationUseCase: AddOperationUseCase,
    private val editOperationUseCase: EditOperationUseCase,
    private val addToMoneyBoxUseCase: AddToMoneyBoxUseCase,
    private val removeFromMoneyBoxUseCase: RemoveFromMoneyBoxUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<FragmentMoneyBoxOperationState>()

    val state: Flow<FragmentMoneyBoxOperationState>
        get() = _state

    fun setData(operationId: Int) {
        viewModelScope.launch {
            val operation = getMoneyBoxOperationUseCase(operationId).first()
            _state.emit(
                FragmentMoneyBoxOperationState.Data(operation.amount.toString())
            )
        }
    }

    fun createMoneyBoxOperation(amountString: String, type: Type) {

        viewModelScope.launch {

            try {
                checkEmptyFields(amountString)
                checkIncorrectNumbers(amountString)

                val amount = amountString.toInt()
                val moneyBoxOperation = MoneyBoxOperation(
                    type,
                    getCurrentTimeMillis(),
                    amount
                )
                addOperationUseCase(moneyBoxOperation)
                handleCreateOperationType(type, amount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editMoneyBoxOperation(newAmountString: String, operationId: Int) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newAmountString)
                checkIncorrectNumbers(newAmountString)

                val oldData = getMoneyBoxOperationUseCase(operationId).first()
                val newAmount = newAmountString.toInt()

                checkNoChanges(
                    listOf(newAmount),
                    listOf(oldData.amount)
                )

                val moneyBoxOperation = MoneyBoxOperation(
                    oldData.type,
                    oldData.dateTimeMillis,
                    newAmount,
                    oldData.id
                )
                editOperationUseCase(moneyBoxOperation)
                handleEditOperationType(oldData.type, newAmount, oldData.amount)

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

    private suspend fun handleCreateOperationType(type: Type, amount: Int) {

        when (type) {
            Type.INCOME -> {
                addMoney(amount)
            }

            Type.EXPENSE -> {
                removeMoney(amount)
            }
        }
    }

    private suspend fun handleEditOperationType(type: Type, oldAmount: Int, newAmount: Int) {

        val difference = abs(newAmount - oldAmount)

        when (type) {
            Type.INCOME -> {
                if (newAmount > oldAmount) {
                    addMoney(difference)
                } else if (oldAmount > newAmount) {
                    removeMoney(difference)
                }
            }

            Type.EXPENSE -> {
                if (newAmount > oldAmount) {
                    removeMoney(difference)
                } else if (oldAmount > newAmount) {
                    addMoney(difference)
                }
            }
        }
    }

    private suspend fun addMoney(amount: Int) {
        addToMoneyBoxUseCase(amount)
        addToBalanceUseCase(amount)
    }

    private suspend fun removeMoney(amount: Int) {
        removeFromMoneyBoxUseCase(amount)
        removeFromBalanceUseCase(amount)
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentMoneyBoxOperationState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentMoneyBoxOperationState.EmptyFields)
    }

    private suspend fun setNoChangesState() {
        _state.emit(FragmentMoneyBoxOperationState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentMoneyBoxOperationState.IncorrectNumber)
    }
}