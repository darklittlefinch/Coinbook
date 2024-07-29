package com.elliemoritz.coinbook.presentation.states

sealed class SettingsState {
    class Balance(val value: String) : SettingsState()
    class CurrencyPosition(val value: Int) : SettingsState()
    class Notifications(val enabled: Boolean) : SettingsState()
    class NotificationsSounds(val enabled: Boolean) : SettingsState()
    data object Finish : SettingsState()
}
