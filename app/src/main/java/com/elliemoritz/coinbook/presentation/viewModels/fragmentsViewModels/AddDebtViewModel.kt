package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.AddDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.EditDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.debtsUseCases.GetDebtUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.AddToBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.RemoveFromBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentDebtState
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

class AddDebtViewModel @Inject constructor(
    private val getDebtUseCase: GetDebtUseCase,
    private val addDebtUseCase: AddDebtUseCase,
    private val editDebtUseCase: EditDebtUseCase,
    private val addToBalanceUseCase: AddToBalanceUseCase,
    private val removeFromBalanceUseCase: RemoveFromBalanceUseCase
) : ViewModel() {

    private var dataFlow = MutableSharedFlow<Debt>()

    private val dataStateFlow = dataFlow
        .map { FragmentDebtState.Data(it.amount.toString(), it.creditor) }

    private val _state = MutableSharedFlow<FragmentDebtState>()

    val state: Flow<FragmentDebtState>
        get() = _state
            .mergeWith(dataStateFlow)

    fun setData(id: Int) {
        viewModelScope.launch {
            val debt = getDebtUseCase(id).first()
            dataFlow.emit(debt)
        }
    }

    fun createDebt(amountString: String, creditor: String) {
        viewModelScope.launch {

            if (amountString.isEmpty() || creditor.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val amount = amountString.toInt()
                val debt = Debt(amount, creditor)
                addDebtUseCase(debt)
                addToBalance(amount)
                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editDebt(amountString: String, creditor: String) {
        viewModelScope.launch {

            if (amountString.isEmpty() || creditor.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val amount = amountString.toInt()
                val oldData = dataFlow.first()
                val debt = Debt(amount, creditor, oldData.id)
                editDebtUseCase(debt)
                handleBalance(oldData.amount, amount)
                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    private suspend fun handleBalance(oldBalance: Int, newBalance: Int) {
        val difference = abs(oldBalance - newBalance)
        if (newBalance > oldBalance) {
            addToBalance(difference)
        } else if (oldBalance > newBalance) {
            removeFromBalance(difference)
        }
    }

    private suspend fun addToBalance(amount: Int) {
        addToBalanceUseCase(amount)
    }

    private suspend fun removeFromBalance(amount: Int) {
        removeFromBalanceUseCase(amount)
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentDebtState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentDebtState.EmptyFields)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentDebtState.IncorrectNumber)
    }

}