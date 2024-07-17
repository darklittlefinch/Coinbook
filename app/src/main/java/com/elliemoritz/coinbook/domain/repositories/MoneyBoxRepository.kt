package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.MoneyBox

interface MoneyBoxRepository {
    suspend fun getMoneyBox(id: Int): MoneyBox?
    suspend fun addMoneyBox(moneyBox: MoneyBox)
    suspend fun editMoneyBox(moneyBox: MoneyBox)
    suspend fun removeMoneyBox(moneyBox: MoneyBox)
}
