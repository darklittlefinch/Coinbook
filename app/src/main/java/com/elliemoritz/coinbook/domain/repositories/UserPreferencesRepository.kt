package com.elliemoritz.coinbook.domain.repositories

interface UserPreferencesRepository {

    suspend fun getBalance(): Int
    suspend fun addToBalance(amount: Int)
    suspend fun removeFromBalance(amount: Int)
    suspend fun editBalance(newAmount: Int)

    suspend fun getCurrency(): String
    suspend fun editCurrency(newCurrency: String)

    suspend fun getNotificationsEnabled(): Boolean
    suspend fun editNotificationsEnabled(enabled: Boolean)

    suspend fun getNotificationsSoundsEnabled(): Boolean
    suspend fun editNotificationsSoundsEnabled(enabled: Boolean)
}
