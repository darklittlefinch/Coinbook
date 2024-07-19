package com.elliemoritz.coinbook.data.repositories

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository
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

    override suspend fun getBalance(): Int {
        return readBalanceValue()
    }

    override suspend fun addToBalance(amount: Int) {
        val oldAmount = readBalanceValue()
        val newAmount = oldAmount + amount
        writeBalanceValue(newAmount)
    }

    override suspend fun removeFromBalance(amount: Int) {
        val oldAmount = readBalanceValue()
        val newAmount = oldAmount - amount
        writeBalanceValue(newAmount)
    }

    override suspend fun editBalance(newAmount: Int) {
        writeBalanceValue(newAmount)
    }

    override suspend fun getCurrency(): String {
        return readCurrencyValue()
    }

    override suspend fun editCurrency(newCurrency: String) {
        writeCurrencyValue(newCurrency)
    }

    override suspend fun getNotificationsEnabled(): Boolean {
        return readNotificationsEnabledValue()
    }

    override suspend fun editNotificationsEnabled(enabled: Boolean) {
        writeNotificationsEnabledValue(enabled)
    }

    override suspend fun getNotificationsSoundsEnabled(): Boolean {
        return readNotificationsSoundsEnabledValue()
    }

    override suspend fun editNotificationsSoundsEnabled(enabled: Boolean) {
        writeNotificationsSoundsEnabledValue(enabled)
    }

    private suspend fun readBalanceValue(): Int {
        return dataStore.data.map { preferences ->
            preferences[BALANCE_KEY] ?: DEFAULT_BALANCE
        }.first()
    }

    private suspend fun writeBalanceValue(newValue: Int) {
        dataStore.edit { preferences ->
            preferences[BALANCE_KEY] = newValue
        }
    }

    private suspend fun readCurrencyValue(): String {
        return dataStore.data.map { preferences ->
            preferences[CURRENCY_KEY] ?: DEFAULT_CURRENCY
        }.first()
    }

    private suspend fun writeCurrencyValue(newCurrency: String) {
        dataStore.edit { preferences ->
            preferences[CURRENCY_KEY] = newCurrency
        }
    }

    private suspend fun readNotificationsEnabledValue(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[NOTIFICATIONS_KEY] ?: DEFAULT_NOTIFICATIONS
        }.first()
    }

    private suspend fun writeNotificationsEnabledValue(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = enabled
        }
    }

    private suspend fun readNotificationsSoundsEnabledValue(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[NOTIFICATIONS_SOUNDS_KEY] ?: DEFAULT_NOTIFICATIONS_SOUNDS
        }.first()
    }

    private suspend fun writeNotificationsSoundsEnabledValue(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_SOUNDS_KEY] = enabled
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
