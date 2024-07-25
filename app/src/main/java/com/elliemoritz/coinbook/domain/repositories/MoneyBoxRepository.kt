package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.MoneyBox
import kotlinx.coroutines.flow.Flow

interface MoneyBoxRepository {
    fun getMoneyBox(): Flow<MoneyBox?>
    suspend fun addMoneyBox(moneyBox: MoneyBox)
    suspend fun editMoneyBox(moneyBox: MoneyBox)
    suspend fun removeMoneyBox(moneyBox: MoneyBox)

    suspend fun refreshMoneyBox()
}
