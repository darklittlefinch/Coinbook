package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.operations.Operation

sealed class HistoryState {
    data object NoData : HistoryState()
    data object HasData : HistoryState()
    class OperationsList(val list: List<Operation>) : HistoryState()
    class Currency(val currency: String) : HistoryState()
}
