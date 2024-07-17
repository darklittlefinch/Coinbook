package com.elliemoritz.coinbook.domain.repositories

interface UserPreferencesRepository {

    suspend fun getBalance(): Int
    suspend fun addToBalance(amount: Int)
    suspend fun removeFromBalance(amount: Int)
    suspend fun editBalance(newAmount: Int)
}
