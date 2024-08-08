package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases.AddDebtOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases.EditDebtOperationUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsOperationsUseCases.GetDebtOperationByDebtIdUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.AddDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.EditDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentDebtState
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

class AddDebtViewModel @Inject constructor(
    private val getDebtUseCase: GetDebtUseCase,
    private val addDebtUseCase: AddDebtUseCase,
    private val editDebtUseCase: EditDebtUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase,
    private val getDebtOperationByDebtIdUseCase: GetDebtOperationByDebtIdUseCase,
    private val addDebtOperationUseCase: AddDebtOperationUseCase,
    private val editDebtOperationUseCase: EditDebtOperationUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<FragmentDebtState>()

    val state: Flow<FragmentDebtState>
        get() = _state

    fun setData(id: Long) {

        viewModelScope.launch {
            val debt = getDebtUseCase(id).first()

            _state.emit(
                FragmentDebtState.Data(
                    debt.amount.toString(),
                    debt.creditor
                )
            )
        }
    }

    fun createDebt(amountString: String, creditor: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(amountString, creditor)
                checkIncorrectNumbers(amountString)

                val amount = amountString.toInt()
                val debt = Debt(
                    amount = amount,
                    creditor = creditor,
                    startedMillis = getCurrentTimeMillis(),
                    finished = false
                )
                addDebtUseCase(debt)
                addToBalanceUseCase(amount)
                addDebtOperation(debt)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    private suspend fun addDebtOperation(debt: Debt) {
        val debtOperation = DebtOperation(
            amount = debt.amount,
            type = Type.INCOME,
            debtId = debt.id,
            debtCreditor = debt.creditor,
            dateTimeMillis = getCurrentTimeMillis()
        )
        addDebtOperationUseCase(debtOperation)
    }

    fun editDebt(newAmountString: String, newCreditor: String, id: Long) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newAmountString, newCreditor)
                checkIncorrectNumbers(newAmountString)

                val oldData = getDebtUseCase(id).first()
                val newAmount = newAmountString.toInt()

                checkNoChanges(
                    listOf(newAmount, newCreditor),
                    listOf(oldData.amount, oldData.creditor)
                )

                val debt = Debt(
                    amount = newAmount,
                    creditor = newCreditor,
                    startedMillis = oldData.startedMillis,
                    finished = oldData.finished,
                    id = oldData.id
                )
                editDebtUseCase(debt)
                editDebtOperation(debt)
                handleBalance(newAmount, oldData.amount)

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

    private suspend fun editDebtOperation(debt: Debt) {

        val debtOperation = getDebtOperationByDebtIdUseCase(debt.id).first()

        val newDebtOperation = DebtOperation(
            amount = debt.amount,
            type = Type.INCOME,
            debtId = debt.id,
            debtCreditor = debt.creditor,
            dateTimeMillis = getCurrentTimeMillis(),
            id = debtOperation.id
        )

        editDebtOperationUseCase(newDebtOperation)
    }

    private suspend fun handleBalance(newBalance: Int, oldBalance: Int) {
        val difference = abs(oldBalance - newBalance)
        if (newBalance > oldBalance) {
            addToBalanceUseCase(difference)
        } else if (oldBalance > newBalance) {
            removeFromBalanceUseCase(difference)
        }
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentDebtState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentDebtState.EmptyFields)
    }

    private suspend fun setNoChangesState() {
        _state.emit(FragmentDebtState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentDebtState.IncorrectNumber)
    }

}