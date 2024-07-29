package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentBalanceState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditBalanceViewModel @Inject constructor(
    getBalanceUseCase: GetBalanceUseCase,
    private val editBalanceUseCase: EditBalanceUseCase
) : ViewModel() {

    private val balanceFlow = getBalanceUseCase()

    private val balanceStateFlow = balanceFlow
        .map { FragmentBalanceState.Data(it.toString()) }

    private val _state = MutableSharedFlow<FragmentBalanceState>()

    val state: Flow<FragmentBalanceState>
        get() = _state
            .mergeWith(balanceStateFlow)

    fun editBalance(newAmountString: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newAmountString)
                checkIncorrectNumbers(newAmountString)

                val newAmount = newAmountString.toInt()
                val oldAmount = balanceFlow.first()

                checkNoChanges(
                    listOf(newAmount),
                    listOf(oldAmount)
                )

                editBalanceUseCase(newAmount)
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

    private suspend fun setFinishState() {
        _state.emit(FragmentBalanceState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentBalanceState.EmptyFields)
    }

    private suspend fun setNoChangesState() {
        _state.emit(FragmentBalanceState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentBalanceState.IncorrectNumber)
    }
}
