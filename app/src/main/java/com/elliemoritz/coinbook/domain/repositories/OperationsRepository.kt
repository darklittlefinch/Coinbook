package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Operation

interface OperationsRepository {
    fun getOperationsList(): LiveData<List<Operation>>
    fun getOperation(id: Int): Operation
    fun addOperation(operation: Operation)
    fun editOperation(operation: Operation)
    fun removeOperation(operation: Operation)
}
