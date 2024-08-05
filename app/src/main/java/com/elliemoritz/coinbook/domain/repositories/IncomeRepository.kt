package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.operations.Income
import kotlinx.coroutines.flow.Flow

interface IncomeRepository {

    fun getIncomeList(): Flow<List<Income>>
    fun getIncomeListForMonth(): Flow<List<Income>>
    suspend fun removeAllIncome()

    fun getIncome(id: Int): Flow<Income>
    suspend fun addIncome(operation: Income)
    suspend fun editIncome(operation: Income)
    suspend fun removeIncome(operation: Income)

    fun getTotalIncomeAmountForMonth(): Flow<Int>
}
