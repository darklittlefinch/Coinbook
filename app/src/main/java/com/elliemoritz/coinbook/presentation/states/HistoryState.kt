package com.elliemoritz.coinbook.presentation.states

import android.media.VolumeShaper.Operation

sealed class HistoryState

data object NoHistoryData : HistoryState()

class HistoryData(
    operations: List<Operation>
) : HistoryState()
