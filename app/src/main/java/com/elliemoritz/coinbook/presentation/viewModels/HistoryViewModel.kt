package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.elliemoritz.coinbook.domain.useCases.operationsUseCases.GetOperationsListUseCase
import com.elliemoritz.coinbook.presentation.states.HistoryState
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    getOperationsListUseCase: GetOperationsListUseCase
) : ViewModel() {

    private val incomeListFlow = getOperationsListUseCase()
    private val incomeListStateFlow = incomeListFlow
        .map { HistoryState.OperationsList(it) }
    private val hasDataStateFlow = incomeListFlow
        .map {
            if (it.isEmpty()) {
                HistoryState.NoData
            } else {
                HistoryState.HasData
            }
        }

    private val _state = MutableSharedFlow<HistoryState>()

    val state: Flow<HistoryState>
        get() = _state
            .mergeWith(incomeListStateFlow)
            .mergeWith(hasDataStateFlow)
}