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

    private val oldData = MutableSharedFlow<MoneyBox>()
    private val dataFlow = oldData
        .map {
            FragmentMoneyBoxState.Data(
                it.goalAmount.toString(),
                it.goal
            )
        }

    private val errorFlow = MutableSharedFlow<FragmentMoneyBoxState>()
    private val finishFlow = MutableSharedFlow<FragmentMoneyBoxState>()

    private val _state = MutableSharedFlow<FragmentMoneyBoxState>()
        .mergeWith(dataFlow)
        .mergeWith(errorFlow)
        .mergeWith(finishFlow)

    val state: Flow<FragmentMoneyBoxState>
        get() = _state

    fun setData() {
        viewModelScope.launch {
            val moneyBox = getMoneyBoxUseCase().first()
            moneyBox?.let {
                oldData.emit(it)
            }
        }
    }

    fun createMoneyBox(goalAmountString: String, goal: String) {
        viewModelScope.launch {

            if (goalAmountString.isEmpty() || goal.isEmpty()) {
                setErrorState()
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
                setErrorState()
            }
        }
    }

    fun editMoneyBox(newGoalAmountString: String, newGoal: String) {
        viewModelScope.launch {

            if (newGoalAmountString.isEmpty() || newGoal.isEmpty()) {
                setErrorState()
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
                setErrorState()
            }
        }
    }

    private suspend fun setErrorState() {
        errorFlow.emit(FragmentMoneyBoxState.Error)
    }

    private suspend fun setFinishState() {
        errorFlow.emit(FragmentMoneyBoxState.Finish)
    }
}