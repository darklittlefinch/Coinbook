package com.elliemoritz.coinbook.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditNotificationsEnabledUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.EditNotificationsSoundsEnabledUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetBalanceUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetCurrencyUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetNotificationsEnabledUseCase
import com.elliemoritz.coinbook.domain.useCases.userPreferencesUseCases.GetNotificationsSoundsEnabledUseCase
import com.elliemoritz.coinbook.presentation.states.SettingsData
import com.elliemoritz.coinbook.presentation.states.SettingsState
import com.elliemoritz.coinbook.presentation.states.SettingsShouldClose
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    application: Application,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val editBalanceUseCase: EditBalanceUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val editCurrencyUseCase: EditCurrencyUseCase,
    private val getNotificationsEnabledUseCase: GetNotificationsEnabledUseCase,
    private val editNotificationsEnabledUseCase: EditNotificationsEnabledUseCase,
    private val getNotificationsSoundsEnabledUseCase: GetNotificationsSoundsEnabledUseCase,
    private val editNotificationsSoundsEnabledUseCase: EditNotificationsSoundsEnabledUseCase
) : AndroidViewModel(application) {

    private val _settingsState = MutableLiveData<SettingsState>()
    val settingsState: LiveData<SettingsState>
        get() = _settingsState

    fun setInitialSettingsState(currencies: Array<String>) {
        viewModelScope.launch {
            val currentCurrency = getCurrencyUseCase()
            _settingsState.value = SettingsData(
                balance = getBalanceUseCase().toString(),
                currencyIndex = getCurrencyPosition(currencies, currentCurrency),
                notificationsEnabled = getNotificationsEnabledUseCase(),
                notificationsSoundsEnabled = getNotificationsSoundsEnabledUseCase()
            )
        }
    }

    fun setNewSettings(
        balance: String,
        currency: String,
        notificationsEnabled: Boolean,
        notificationsSoundsEnabled: Boolean
    ) {
        viewModelScope.launch {
            editBalanceUseCase(balance.toInt())
            editCurrencyUseCase(currency)
            editNotificationsEnabledUseCase(notificationsEnabled)
            editNotificationsSoundsEnabledUseCase(notificationsSoundsEnabled)
            closeSettings()
        }
    }

    fun closeSettings() {
        _settingsState.value = SettingsShouldClose
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