package com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentBalanceState
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditBalanceViewModel @Inject constructor(
    getBalanceUseCase: GetBalanceUseCase,
    private val editBalanceUseCase: EditBalanceUseCase
) : ViewModel() {

    private val balanceStateFlow = getBalanceUseCase()
        .map { FragmentBalanceState.Data(it.toString()) }

    private val _state = MutableSharedFlow<FragmentBalanceState>()

    val state: Flow<FragmentBalanceState>
        get() = _state
            .mergeWith(balanceStateFlow)

    fun editBalance(amountString: String) {

        viewModelScope.launch {

            if (amountString.isEmpty()) {
                setErrorState()
                return@launch
            }

            try {
                val amount = amountString.toInt()
                editBalanceUseCase(amount)
                setFinishState()
            } catch (e: NumberFormatException) {
                setErrorState()
            }
        }
    }

    private suspend fun setFinishState() {
        _state.emit(FragmentBalanceState.Finish)
    }

    private suspend fun setErrorState() {
        _state.emit(FragmentBalanceState.Error)
    }
}
