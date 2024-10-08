package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.exceptions.NotEnoughMoneyException
import com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases.AddMoneyBoxOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases.EditMoneyBoxOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxOperationsUseCases.GetMoneyBoxOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.AddToMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.RemoveFromMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentMoneyBoxOperationState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
import com.elliemoritz.coinbook.presentation.util.checkNotEnoughMoney
import com.elliemoritz.coinbook.presentation.util.getCurrentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

class AddMoneyBoxOperationViewModel @Inject constructor(
    private val getMoneyBoxUseCase: GetMoneyBoxUseCase,
    private val getMoneyBoxOperationUseCase: GetMoneyBoxOperationUseCase,
    private val addMoneyBoxOperationUseCase: AddMoneyBoxOperationUseCase,
    private val editMoneyBoxOperationUseCase: EditMoneyBoxOperationUseCase,
    private val addToMoneyBoxUseCase: AddToMoneyBoxUseCase,
    private val removeFromMoneyBoxUseCase: RemoveFromMoneyBoxUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<FragmentMoneyBoxOperationState>()

    val state: Flow<FragmentMoneyBoxOperationState>
        get() = _state

    fun setData(id: Long) {
        viewModelScope.launch {
            val operation = getMoneyBoxOperationUseCase(id).first()
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
                val balance = getBalanceUseCase().first()
                val moneyBoxAmount = getMoneyBoxUseCase().first()?.totalAmount ?: NO_DATA_VALUE

                if (type == Type.INCOME) {
                    checkNotEnoughMoney(amount, moneyBoxAmount)
                    checkNotEnoughMoney(amount, balance)
                }

                val currency = getCurrencyUseCase().first()

                val moneyBoxOperation = MoneyBoxOperation(
                    amount = amount,
                    type = type,
                    dateTimeMillis = getCurrentTimeMillis(),
                    currency = currency
                )

                addMoneyBoxOperationUseCase(moneyBoxOperation)
                handleCreateOperationType(type, amount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: NotEnoughMoneyException) {
                setNotEnoughMoneyState()
            }
        }
    }

    fun editMoneyBoxOperation(newAmountString: String, id: Long) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newAmountString)
                checkIncorrectNumbers(newAmountString)

                val oldData = getMoneyBoxOperationUseCase(id).first()
                val newAmount = newAmountString.toInt()

                checkNoChanges(
                    listOf(newAmount),
                    listOf(oldData.amount)
                )

                if (oldData.type == Type.EXPENSE && newAmount > oldData.amount) {
                    val balance = getBalanceUseCase().first()
                    val moneyBoxAmount = getMoneyBoxUseCase().first()?.totalAmount ?: NO_DATA_VALUE
                    checkNotEnoughMoney(newAmount - oldData.amount, moneyBoxAmount)
                    checkNotEnoughMoney(newAmount - oldData.amount, balance)
                }

                val currency = getCurrencyUseCase().first()

                val moneyBoxOperation = MoneyBoxOperation(
                    amount = newAmount,
                    type = oldData.type,
                    dateTimeMillis = oldData.dateTimeMillis,
                    id = oldData.id,
                    currency = currency
                )

                editMoneyBoxOperationUseCase(moneyBoxOperation)
                handleEditOperationType(oldData.type, newAmount, oldData.amount)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            } catch (e: NoChangesException) {
                setNoChangesState()
            } catch (e: NotEnoughMoneyException) {
                setNotEnoughMoneyState()
            }
        }
    }

    private suspend fun handleCreateOperationType(type: Type, amount: Int) {

        when (type) {
            Type.EXPENSE -> {
                addMoneyToMoneyBox(amount)
            }

            Type.INCOME -> {
                removeMoney(amount)
            }
        }
    }

    private suspend fun handleEditOperationType(type: Type, oldAmount: Int, newAmount: Int) {

        val difference = abs(newAmount - oldAmount)

        when (type) {
            Type.EXPENSE -> {
                if (newAmount > oldAmount) {
                    addMoneyToMoneyBox(difference)
                } else if (oldAmount > newAmount) {
                    removeMoney(difference)
                }
            }

            Type.INCOME -> {
                if (newAmount > oldAmount) {
                    removeMoney(difference)
                } else if (oldAmount > newAmount) {
                    addMoneyToMoneyBox(difference)
                }
            }
        }
    }

    private suspend fun addMoneyToMoneyBox(amount: Int) {
        addToMoneyBoxUseCase(amount)
        removeFromBalanceUseCase(amount)
    }

    private suspend fun removeMoney(amount: Int) {
        removeFromMoneyBoxUseCase(amount)
        addToBalanceUseCase(amount)
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

    private suspend fun setNotEnoughMoneyState() {
        _state.emit(FragmentMoneyBoxOperationState.NotEnoughMoney)
    }

    companion object {
        private const val NO_DATA_VALUE = 0
    }
}