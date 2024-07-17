package com.elliemoritz.coinbook.presentation.states

import com.elliemoritz.coinbook.domain.entities.Alarm

sealed class AlarmState

data object NoAlarmsData : AlarmState()

class AlarmData(
    nextAlarm: String,
    alarms: List<Alarm>
) : AlarmState()
