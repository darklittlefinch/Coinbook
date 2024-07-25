package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.AddMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.EditMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentMoneyBoxState
import com.elliemoritz.coinbook.presentation.util.getCurrentTimestamp
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddMoneyBoxViewModel @Inject constructor(
    private val getMoneyBoxUseCase: GetMoneyBoxUseCase,
    private val addMoneyBoxUseCase: AddMoneyBoxUseCase,
    private val editMoneyBoxUseCase: EditMoneyBoxUseCase
) : ViewModel() {

    private val dataFlow = MutableSharedFlow<MoneyBox>()

    private val dataStateFlow = dataFlow
        .map {
            FragmentMoneyBoxState.Data(
                it.goalAmount.toString(),
                it.goal
            )
        }

    private val _state = MutableSharedFlow<FragmentMoneyBoxState>()

    val state: Flow<FragmentMoneyBoxState>
        get() = _state
            .mergeWith(dataStateFlow)

    fun setData() {
        viewModelScope.launch {
            val moneyBox = getMoneyBoxUseCase().first()
            moneyBox?.let {
                dataFlow.emit(it)
            }
        }
    }

    fun createMoneyBox(goalAmountString: String, goal: String) {
        viewModelScope.launch {

            if (goalAmountString.isEmpty() || goal.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val goalAmount = goalAmountString.toInt()
                val moneyBox = MoneyBox(
                    goalAmount,
                    goal,
                    getCurrentTimestamp()
                )
                addMoneyBoxUseCase(moneyBox)

                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editMoneyBox(newGoalAmountString: String, newGoal: String) {
        viewModelScope.launch {

            if (newGoalAmountString.isEmpty() || newGoal.isEmpty()) {
                setEmptyFieldsState()
                return@launch
            }

            try {
                val goalAmount = newGoalAmountString.toInt()
                val moneyBox = MoneyBox(
                    goalAmount,
                    newGoal,
                    getCurrentTimestamp()
                )
                editMoneyBoxUseCase(moneyBox)

                setFinishState()
            } catch (e: NumberFormatException) {
                setIncorrectNumberState()
            }
        }
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentMoneyBoxState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentMoneyBoxState.EmptyFields)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentMoneyBoxState.IncorrectNumber)
    }
}