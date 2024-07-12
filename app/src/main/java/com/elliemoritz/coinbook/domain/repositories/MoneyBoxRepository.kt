package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.MoneyBox

interface MoneyBoxRepository {
    fun getMoneyBox(id: Int): MoneyBox
    fun addMoneyBox(moneyBox: MoneyBox)
    fun editMoneyBox(moneyBox: MoneyBox)
    fun removeMoneyBox(moneyBox: MoneyBox)
}
