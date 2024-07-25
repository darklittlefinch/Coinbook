package com.elliemoritz.coinbook.data.repositories

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    application: Application
) : UserPreferencesRepository {

    private val Application.dataStore by preferencesDataStore(
        name = PREFERENCES_NAME
    )
    private val dataStore = application.dataStore

    private val refreshBalanceEvents = MutableSharedFlow<Unit>()
    private val refreshCurrencyEvents = MutableSharedFlow<Unit>()

    override fun getBalance(): Flow<Int> = getBalanceFlow()

    override suspend fun refreshUserPreferencesData() {
        refreshBalanceEvents.emit(Unit)
        refreshCurrencyEvents.emit(Unit)
    }

    override suspend fun addToBalance(amount: Int) {
        val oldAmount = getBalanceFlow().first()
        val newAmount = oldAmount + amount
        writeBalanceValue(newAmount)
    }

    override suspend fun removeFromBalance(amount: Int) {
        val oldAmount = getBalanceFlow().first()
        val newAmount = oldAmount - amount
        writeBalanceValue(newAmount)
    }

    override suspend fun editBalance(newAmount: Int) {
        writeBalanceValue(newAmount)
    }

    override fun getCurrency(): Flow<String> = dataStore.data.map { preferences ->
        preferences[CURRENCY_KEY] ?: DEFAULT_CURRENCY
    }

    override suspend fun editCurrency(newCurrency: String) {
        dataStore.edit { preferences ->
            preferences[CURRENCY_KEY] = newCurrency
        }
    }

    override fun getNotificationsEnabled(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_KEY] ?: DEFAULT_NOTIFICATIONS
    }

    override suspend fun editNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = enabled
        }
    }

    override fun getNotificationsSoundsEnabled(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[NOTIFICATIONS_SOUNDS_KEY] ?: DEFAULT_NOTIFICATIONS_SOUNDS
        }

    override suspend fun editNotificationsSoundsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_SOUNDS_KEY] = enabled
        }
    }

    private fun getBalanceFlow(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[BALANCE_KEY] ?: DEFAULT_BALANCE
    }

    private suspend fun writeBalanceValue(newValue: Int) {
        dataStore.edit { preferences ->
            preferences[BALANCE_KEY] = newValue
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "settings"

        private val BALANCE_KEY = intPreferencesKey("amount")
        private val CURRENCY_KEY = stringPreferencesKey("currency")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
        private val NOTIFICATIONS_SOUNDS_KEY = booleanPreferencesKey("notifications_sounds")

        private const val DEFAULT_BALANCE = 0
        private const val DEFAULT_CURRENCY = "$"
        private const val DEFAULT_NOTIFICATIONS = true
        private const val DEFAULT_NOTIFICATIONS_SOUNDS = false
    }
}
