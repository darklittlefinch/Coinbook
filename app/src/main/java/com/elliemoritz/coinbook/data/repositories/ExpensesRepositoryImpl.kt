package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.ExpensesDao
import com.elliemoritz.coinbook.data.mappers.ExpenseMapper
import com.elliemoritz.coinbook.data.util.getBeginOfMonthMillis
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(
    private val dao: ExpensesDao,
    private val mapper: ExpenseMapper
) : ExpensesRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getExpensesList(): Flow<List<Expense>> = flow {
        val dbModelsList = dao.getOperationsList()
        val operationsList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(operationsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getOperationsList()
            val updatedOperationsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedOperationsList)
        }
    }

    override fun getExpensesListForMonth(): Flow<List<Expense>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val dbModelsList = dao.getOperationsListFromDate(beginOfMonthMillis)
        val expensesList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(expensesList)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedDbModelsList = dao.getOperationsListFromDate(updatedBeginOfMonthMillis)
            val updatedExpensesList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedExpensesList)
        }
    }

    override fun getCategoryExpensesListForMonth(categoryId: Int): Flow<List<Expense>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val dbModelsList = dao.getCategoryExpensesListFromDate(categoryId, beginOfMonthMillis)
        val expensesList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(expensesList)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedDbModelsList = dao.getCategoryExpensesListFromDate(
                categoryId,
                updatedBeginOfMonthMillis
            )
            val updatedExpensesList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedExpensesList)
        }
    }

    override suspend fun removeAllExpenses() {
        dao.removeAllOperations()
    }

    override fun getExpense(id: Int): Flow<Expense> = flow {
        val dbModel = dao.getOperation(id)
        val operation = mapper.mapDbModelToEntity(dbModel)
        emit(operation)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedOperation = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedOperation)
        }
    }

    override suspend fun addExpense(operation: Expense) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun editExpense(operation: Expense) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun removeExpense(operation: Expense) {
        dao.removeOperation(operation.id)
        refreshData()
    }

    override fun getTotalExpensesAmountForMonth(): Flow<Int> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val expensesAmount = dao.getOperationsAmountFromDate(beginOfMonthMillis)
            ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
        emit(expensesAmount)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedExpensesAmount = dao.getOperationsAmountFromDate(updatedBeginOfMonthMillis)
                ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
            emit(updatedExpensesAmount)
        }
    }

    override fun getTotalExpensesAmountByCategoryForMonth(categoryId: Int): Flow<Int> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val expensesAmount = dao.getOperationsAmountByCategoryFromDate(
            categoryId,
            beginOfMonthMillis
        )
            ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
        emit(expensesAmount)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedExpensesAmount = dao.getOperationsAmountByCategoryFromDate(
                categoryId,
                updatedBeginOfMonthMillis
            )
                ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
            emit(updatedExpensesAmount)
        }
    }

    companion object {
        private const val OPERATIONS_AMOUNT_DEFAULT_VALUE = 0
    }
}
