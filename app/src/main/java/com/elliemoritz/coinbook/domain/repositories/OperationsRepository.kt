package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import java.sql.Timestamp

interface OperationsRepository {
    fun getOperationsList(): LiveData<List<Operation>>
    suspend fun getOperation(id: Int): Operation
    suspend fun addOperation(operation: Operation)
    suspend fun editOperation(operation: Operation)
    suspend fun removeOperation(operation: Operation)
    suspend fun removeAllOperations()

    fun getIncomeListFromDate(date: Timestamp): LiveData<List<Income>>
    fun getExpensesListFromDate(date: Timestamp): LiveData<List<Expense>>
    fun getMoneyBoxOperationsList(): LiveData<List<MoneyBoxOperation>>
    fun getDebtOperationsList(): LiveData<List<DebtOperation>>

    fun getCategoryExpensesListFromDate(
        categoryName: String,
        date: Timestamp
    ): LiveData<List<Expense>>
}
