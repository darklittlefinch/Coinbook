package com.elliemoritz.coinbook.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditNotificationsEnabledUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditNotificationsSoundsEnabledUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetNotificationsEnabledUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetNotificationsSoundsEnabledUseCase
import com.elliemoritz.coinbook.presentation.states.SettingsState
import com.elliemoritz.coinbook.presentation.util.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    getBalanceUseCase: GetBalanceUseCase,
    private val editBalanceUseCase: EditBalanceUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val editCurrencyUseCase: EditCurrencyUseCase,
    getNotificationsEnabledUseCase: GetNotificationsEnabledUseCase,
    private val editNotificationsEnabledUseCase: EditNotificationsEnabledUseCase,
    getNotificationsSoundsEnabledUseCase: GetNotificationsSoundsEnabledUseCase,
    private val editNotificationsSoundsEnabledUseCase: EditNotificationsSoundsEnabledUseCase
) : ViewModel() {

    private val balanceStateFlow = getBalanceUseCase()
        .map { SettingsState.Balance(it.toString()) }

    private val notificationsStateFlow = getNotificationsEnabledUseCase()
        .map { SettingsState.Notifications(it) }

    private val notificationsSoundsStateFlow = getNotificationsSoundsEnabledUseCase()
        .map { SettingsState.NotificationsSounds(it) }

    private val _state = MutableSharedFlow<SettingsState>()

    val state: Flow<SettingsState>
        get() = _state
            .mergeWith(balanceStateFlow)
            .mergeWith(notificationsStateFlow)
            .mergeWith(notificationsSoundsStateFlow)

    fun setCurrencyPosition(currencies: Array<String>) {
        viewModelScope.launch {
            val currentCurrency = getCurrencyUseCase().first()
            val currencyPosition = getCurrencyPosition(currencies, currentCurrency)
            _state.emit(SettingsState.CurrencyPosition(currencyPosition))
        }
    }

    fun setNewSettings(
        balance: String,
        currency: String,
        notificationsEnabled: Boolean,
        notificationsSoundsEnabled: Boolean
    ) {
        viewModelScope.launch {
            if (balance.isNotBlank()) {
                editBalanceUseCase(balance.toInt())
            }
            editCurrencyUseCase(currency)
            editNotificationsEnabledUseCase(notificationsEnabled)
            editNotificationsSoundsEnabledUseCase(notificationsSoundsEnabled)
            closeSettings()
        }
    }

    fun closeSettings() {
        viewModelScope.launch {
            _state.emit(SettingsState.Finish)
        }
    }

    private fun getCurrencyPosition(currencies: Array<String>, currentCurrency: String): Int {
        for (i in currencies.indices) {
            if (currencies[i] == currentCurrency) {
                return i
            }
        }
        return DEFAULT_CURRENCY_POSITION
    }

    companion object {
        private const val DEFAULT_CURRENCY_POSITION = 0
    }
}