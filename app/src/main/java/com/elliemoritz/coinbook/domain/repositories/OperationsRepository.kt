package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.operations.Operation
import kotlinx.coroutines.flow.Flow

interface OperationsRepository {
    fun getAllOperationsList(): Flow<List<Operation>>
    suspend fun removeAllOperations()
}