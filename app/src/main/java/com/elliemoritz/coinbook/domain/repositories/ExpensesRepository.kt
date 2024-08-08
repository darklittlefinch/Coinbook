package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.operations.Expense
import kotlinx.coroutines.flow.Flow

interface ExpensesRepository {

    fun getExpensesList(): Flow<List<Expense>>
    fun getExpensesListForMonth(): Flow<List<Expense>>
    fun getCategoryExpensesListForMonth(categoryId: Long): Flow<List<Expense>>
    suspend fun removeAllExpenses()

    fun getExpense(id: Long): Flow<Expense>
    suspend fun addExpense(operation: Expense)
    suspend fun editExpense(operation: Expense)
    suspend fun removeExpense(operation: Expense)

    fun getTotalExpensesAmountForMonth(): Flow<Int>
    fun getTotalExpensesAmountByCategoryForMonth(categoryId: Long): Flow<Int>
}
