package com.elliemoritz.coinbook.data.repositories

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    private suspend fun readBalanceValue(): Int {
        return dataStore.data.map { preferences ->
            preferences[BALANCE_KEY] ?: 0
        }.first()
    }

    private suspend fun writeBalanceValue(newValue: Int) {
        dataStore.edit { preferences ->
            preferences[BALANCE_KEY] = newValue
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "balance"
        private val BALANCE_KEY = intPreferencesKey("amount")
    }
}
