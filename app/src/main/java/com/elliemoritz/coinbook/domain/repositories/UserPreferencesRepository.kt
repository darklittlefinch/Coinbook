package com.elliemoritz.coinbook.domain.repositories

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    fun getBalance(): Flow<Int>
    suspend fun addToBalance(amount: Int)
    suspend fun removeFromBalance(amount: Int)
    suspend fun editBalance(newAmount: Int)

    fun getCurrency(): Flow<String>
    suspend fun editCurrency(newCurrency: String)

    fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun editNotificationsEnabled(enabled: Boolean)

    fun getNotificationsSoundsEnabled(): Flow<Boolean>
    suspend fun editNotificationsSoundsEnabled(enabled: Boolean)

    suspend fun refreshUserPreferencesData()
}
