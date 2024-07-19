package com.elliemoritz.coinbook.presentation.states

sealed class SettingsState

class SettingsData(
    val balance: String,
    val currencyIndex: Int,
    val notificationsEnabled: Boolean,
    val notificationsSoundsEnabled: Boolean
) : SettingsState()

data object SettingsShouldClose: SettingsState()
