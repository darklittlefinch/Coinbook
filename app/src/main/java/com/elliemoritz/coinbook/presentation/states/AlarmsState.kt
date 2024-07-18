package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Alarm

sealed class AlarmsState

data object NoAlarms : AlarmsState()

class AlarmsData(
    nextAlarm: String,
    alarms: List<Alarm>
) : AlarmsState()
