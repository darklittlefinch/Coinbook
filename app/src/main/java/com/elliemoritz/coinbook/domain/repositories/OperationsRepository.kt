package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

interface OperationsRepository {
    fun getOperationsList(): Flow<List<Operation>>
    fun getOperation(id: Int): Flow<Operation>
    suspend fun addOperation(operation: Operation)
    suspend fun editOperation(operation: Operation)
    suspend fun removeOperation(operation: Operation)
    suspend fun removeAllOperations()

    fun getIncomeListForMonth(): Flow<List<Income>>
    fun getExpensesListForMonth(): Flow<List<Expense>>
    fun getMoneyBoxOperationsListFromDate(date: Timestamp): Flow<List<MoneyBoxOperation>>
    fun getDebtOperationsList(): Flow<List<DebtOperation>>

    fun getTotalIncomeAmountForMonth(): Flow<Int>
    fun getTotalExpensesAmountForMonth(): Flow<Int>

    fun getCategoryExpensesListForMonth(categoryName: String): Flow<List<Expense>>

    suspend fun refreshOperationData()
}
