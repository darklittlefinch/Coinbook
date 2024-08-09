package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.Debt
import kotlinx.coroutines.flow.Flow

interface DebtsRepository {
    fun getDebtsList(): Flow<List<Debt>>
    fun getDebt(id: Long): Flow<Debt>
    suspend fun addDebt(debt: Debt)
    suspend fun editDebt(debt: Debt)
    suspend fun removeDebt(debt: Debt)
    fun getTotalDebtsAmount(finished: Boolean): Flow<Int>
}
