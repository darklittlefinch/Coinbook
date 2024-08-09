package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import kotlinx.coroutines.flow.Flow

interface MoneyBoxOperationsRepository {

    fun getOperationsList(): Flow<List<MoneyBoxOperation>>
    fun getOperationsListFromDate(dateTimeMillis: Long): Flow<List<MoneyBoxOperation>>
    suspend fun removeAllOperations()

    fun getOperation(id: Long): Flow<MoneyBoxOperation>
    suspend fun addOperation(operation: MoneyBoxOperation)
    suspend fun editOperation(operation: MoneyBoxOperation)
    suspend fun removeOperation(operation: MoneyBoxOperation)
}
