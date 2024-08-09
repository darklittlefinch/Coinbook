package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import kotlinx.coroutines.flow.Flow

interface DebtsOperationsRepository {

    fun getOperationsList(): Flow<List<DebtOperation>>
    suspend fun removeAllOperations()

    fun getOperation(id: Long): Flow<DebtOperation>
    fun getOperationByDebtId(debtId: Long): Flow<DebtOperation>
    suspend fun addOperation(operation: DebtOperation)
    suspend fun editOperation(operation: DebtOperation)
    suspend fun removeOperation(operation: DebtOperation)
}
