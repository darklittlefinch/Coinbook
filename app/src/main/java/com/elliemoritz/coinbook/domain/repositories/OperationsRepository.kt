package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import kotlinx.coroutines.flow.Flow

interface OperationsRepository {
    fun getOperationsList(): Flow<List<Operation>>
    fun getOperation(id: Int): Flow<Operation>
    suspend fun addOperation(operation: Operation)
    suspend fun editOperation(operation: Operation)
    suspend fun removeOperation(operation: Operation)
    suspend fun removeAllOperations()

    fun getIncome(id: Int): Flow<Income>
    fun getExpense(id: Int): Flow<Expense>
    fun getMoneyBoxOperation(id: Int): Flow<MoneyBoxOperation>
    fun getDebtOperation(id: Int): Flow<DebtOperation>

    fun getIncomeListForMonth(): Flow<List<Income>>
    fun getExpensesListForMonth(): Flow<List<Expense>>
    fun getMoneyBoxOperationsListFromDate(dateTimeMillis: Long): Flow<List<MoneyBoxOperation>>
    fun getDebtOperationsList(): Flow<List<DebtOperation>>

    fun getTotalIncomeAmountForMonth(): Flow<Int>
    fun getTotalExpensesAmountForMonth(): Flow<Int>

    fun getCategoryExpensesListForMonth(categoryName: String): Flow<List<Expense>>
}
