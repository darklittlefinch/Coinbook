package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.exceptions.EmptyFieldsException
import com.elliemoritz.coinbook.domain.exceptions.IncorrectNumberException
import com.elliemoritz.coinbook.domain.exceptions.NoChangesException
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.AddMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.EditMoneyBoxUseCase
import com.elliemoritz.coinbook.domain.useCases.moneyBoxUseCases.GetMoneyBoxUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentMoneyBoxState
import com.elliemoritz.coinbook.presentation.util.checkEmptyFields
import com.elliemoritz.coinbook.presentation.util.checkIncorrectNumbers
import com.elliemoritz.coinbook.presentation.util.checkNoChanges
import com.elliemoritz.coinbook.presentation.util.getCurrentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddMoneyBoxViewModel @Inject constructor(
    private val getMoneyBoxUseCase: GetMoneyBoxUseCase,
    private val addMoneyBoxUseCase: AddMoneyBoxUseCase,
    private val editMoneyBoxUseCase: EditMoneyBoxUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<FragmentMoneyBoxState>()

    val state: Flow<FragmentMoneyBoxState>
        get() = _state

    fun setData() {

        viewModelScope.launch {
            val moneyBox = getMoneyBoxUseCase().first()

            moneyBox?.let {
                _state.emit(
                    FragmentMoneyBoxState.Data(
                        it.goalAmount.toString(),
                        it.goal
                    )
                )
            }
        }
    }

    fun createMoneyBox(goalAmountString: String, goal: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(goalAmountString, goal)
                checkIncorrectNumbers(goalAmountString)

                val goalAmount = goalAmountString.toInt()
                val moneyBox = MoneyBox(
                    goalAmount,
                    goal,
                    getCurrentTimeMillis()
                )
                addMoneyBoxUseCase(moneyBox)

                setFinishState()

            } catch (e: EmptyFieldsException) {
                setEmptyFieldsState()
            } catch (e: IncorrectNumberException) {
                setIncorrectNumberState()
            }
        }
    }

    fun editMoneyBox(newGoalAmountString: String, newGoal: String) {

        viewModelScope.launch {

            try {
                checkEmptyFields(newGoalAmountString, newGoal)
                checkIncorrectNumbers(newGoalAmountString)

                val oldData = getMoneyBoxUseCase().first()
                    ?: throw RuntimeException("Money box does not exist")

                val newGoalAmount = newGoalAmountString.toInt()

                checkNoChanges(
                    listOf(newGoalAmount, newGoal),
                    listOf(oldData.goalAmount, oldData.goal)
                )

                val moneyBox = MoneyBox(
                    newGoalAmount,
                    newGoal,
                    oldData.startedMillis
                )
                editMoneyBoxUseCase(moneyBox)

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
        _state.emit(FragmentMoneyBoxState.Finish)
    }

    private suspend fun setEmptyFieldsState() {
        _state.emit(FragmentMoneyBoxState.EmptyFields)
    }

    private suspend fun setNoChangesState() {
        _state.emit(FragmentMoneyBoxState.NoChanges)
    }

    private suspend fun setIncorrectNumberState() {
        _state.emit(FragmentMoneyBoxState.IncorrectNumber)
    }
}