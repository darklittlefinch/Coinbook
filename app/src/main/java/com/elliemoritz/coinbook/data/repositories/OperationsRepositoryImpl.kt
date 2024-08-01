package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.OperationsDao
import com.elliemoritz.coinbook.data.mappers.OperationsMapper
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_DEBT
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_MONEY_BOX_OPERATION
import com.elliemoritz.coinbook.data.util.TYPE_EXPENSE
import com.elliemoritz.coinbook.data.util.TYPE_INCOME
import com.elliemoritz.coinbook.data.util.getBeginOfMonthMillis
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OperationsRepositoryImpl @Inject constructor(
    private val dao: OperationsDao,
    private val mapper: OperationsMapper
) : OperationsRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getOperationsList(): Flow<List<Operation>> = flow {
        val dbModelsList = dao.getOperationsList()
        val operationsList = mapper.mapListDbModelToListOperations(dbModelsList)
        emit(operationsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getOperationsList()
            val updatedOperationsList = mapper.mapListDbModelToListOperations(updatedDbModelsList)
            emit(updatedOperationsList)
        }
    }

    override fun getOperation(id: Int): Flow<Operation> = flow {
        val dbModel = dao.getOperation(id)
        val operation = mapper.mapDbModelToOperation(dbModel)
        emit(operation)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedOperation = mapper.mapDbModelToOperation(updatedDbModel)
            emit(updatedOperation)
        }
    }

    override suspend fun addOperation(operation: Operation) {
        val dbModel = mapper.mapOperationToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun editOperation(operation: Operation) {
        val dbModel = mapper.mapOperationToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun removeOperation(operation: Operation) {
        dao.removeOperation(operation.id)
        refreshData()
    }

    override suspend fun removeAllOperations() {
        dao.removeAllOperations()
    }

    override fun getIncome(id: Int): Flow<Income> = flow {
        val dbModel = dao.getOperation(id)
        val income = mapper.mapDbModelToIncome(dbModel)
        emit(income)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedIncome = mapper.mapDbModelToIncome(updatedDbModel)
            emit(updatedIncome)
        }
    }

    override fun getExpense(id: Int): Flow<Expense> = flow {
        val dbModel = dao.getOperation(id)
        val expense = mapper.mapDbModelToExpense(dbModel)
        emit(expense)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedExpense = mapper.mapDbModelToExpense(updatedDbModel)
            emit(updatedExpense)
        }
    }

    override fun getMoneyBoxOperation(id: Int): Flow<MoneyBoxOperation> = flow {
        val dbModel = dao.getOperation(id)
        val moneyBoxOperation = mapper.mapDbModelToMoneyBoxOperation(dbModel)
        emit(moneyBoxOperation)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedMoneyBoxOperation = mapper.mapDbModelToMoneyBoxOperation(updatedDbModel)
            emit(updatedMoneyBoxOperation)
        }
    }

    override fun getDebtOperation(id: Int): Flow<DebtOperation> = flow {
        val dbModel = dao.getOperation(id)
        val debtOperation = mapper.mapDbModelToDebtOperation(dbModel)
        emit(debtOperation)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedDebtOperation = mapper.mapDbModelToDebtOperation(updatedDbModel)
            emit(updatedDebtOperation)
        }
    }

    override fun getIncomeListForMonth(): Flow<List<Income>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val dbModelsList = dao.getOperationsListByType(TYPE_INCOME, beginOfMonthMillis)
        val incomeList = mapper.mapListDbModelToListIncome(dbModelsList)
        emit(incomeList)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedDbModelsList = dao.getOperationsListByType(
                TYPE_INCOME,
                updatedBeginOfMonthMillis
            )
            val updatedIncomeList = mapper.mapListDbModelToListIncome(updatedDbModelsList)
            emit(updatedIncomeList)
        }
    }

    override fun getExpensesListForMonth(): Flow<List<Expense>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val dbModelsList = dao.getOperationsListByType(TYPE_EXPENSE, beginOfMonthMillis)
        val expensesList = mapper.mapListDbModelToListExpenses(dbModelsList)
        emit(expensesList)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedDbModelsList = dao.getOperationsListByType(
                TYPE_EXPENSE,
                updatedBeginOfMonthMillis
            )
            val updatedExpensesList = mapper.mapListDbModelToListExpenses(updatedDbModelsList)
            emit(updatedExpensesList)
        }
    }

    override fun getMoneyBoxOperationsListFromDate(
        dateTimeMillis: Long
    ): Flow<List<MoneyBoxOperation>> = flow {
        val dbModelsList = dao.getOperationsListByOperationForm(
            OPERATION_FORM_MONEY_BOX_OPERATION,
            dateTimeMillis
        )
        val moneyBoxOperationsList = mapper.mapListDbModelToListMoneyBoxOperations(dbModelsList)
        emit(moneyBoxOperationsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getOperationsListByOperationForm(
                OPERATION_FORM_MONEY_BOX_OPERATION,
                dateTimeMillis
            )
            val updatedMoneyBoxOperationsList = mapper.mapListDbModelToListMoneyBoxOperations(
                updatedDbModelsList
            )
            emit(updatedMoneyBoxOperationsList)
        }
    }

    override fun getDebtOperationsList(): Flow<List<DebtOperation>> = flow {
        val dbModelsList = dao.getOperationsListByOperationForm(OPERATION_FORM_DEBT)
        val debtsOperationsList = mapper.mapListDbModelToListDebtOperations(dbModelsList)
        emit(debtsOperationsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getOperationsListByOperationForm(OPERATION_FORM_DEBT)
            val updatedDebtsOperationsList = mapper.mapListDbModelToListDebtOperations(
                updatedDbModelsList
            )
            emit(updatedDebtsOperationsList)
        }
    }

    override fun getTotalIncomeAmountForMonth(): Flow<Int> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val incomeAmount = dao.getOperationsAmountByType(
            TYPE_INCOME,
            beginOfMonthMillis
        ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
        emit(incomeAmount)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedIncomeAmount = dao.getOperationsAmountByType(
                TYPE_INCOME,
                updatedBeginOfMonthMillis
            ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
            emit(updatedIncomeAmount)
        }
    }

    override fun getTotalExpensesAmountForMonth(): Flow<Int> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val expensesAmount = dao.getOperationsAmountByType(
            TYPE_EXPENSE,
            beginOfMonthMillis
        ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
        emit(expensesAmount)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedExpensesAmount = dao.getOperationsAmountByType(
                TYPE_EXPENSE,
                updatedBeginOfMonthMillis
            ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
            emit(updatedExpensesAmount)
        }
    }

    override fun getCategoryExpensesListForMonth(categoryName: String): Flow<List<Expense>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val dbModelsList = dao.getCategoryExpensesList(categoryName, beginOfMonthMillis)
        val expensesList = mapper.mapListDbModelToListExpenses(dbModelsList)
        emit(expensesList)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedDbModelsList = dao.getCategoryExpensesList(categoryName, updatedBeginOfMonthMillis)
            val updatedExpensesList = mapper.mapListDbModelToListExpenses(updatedDbModelsList)
            emit(updatedExpensesList)
        }
    }

    companion object {
        private const val OPERATIONS_AMOUNT_DEFAULT_VALUE = 0
    }
}
