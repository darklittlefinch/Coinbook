package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Debt

interface DebtsRepository {
    fun getDebtsList(): LiveData<List<Debt>>
    suspend fun getDebt(id: Int): Debt
    suspend fun addDebt(debt: Debt)
    suspend fun editDebt(debt: Debt)
    suspend fun removeDebt(debt: Debt)
    suspend fun getTotalDebtsAmount(): Int
}
