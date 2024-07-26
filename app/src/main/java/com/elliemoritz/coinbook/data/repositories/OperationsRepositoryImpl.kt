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
import java.sql.Timestamp
import javax.inject.Inject

class OperationsRepositoryImpl @Inject constructor(
    private val dao: OperationsDao,
    private val mapper: OperationsMapper
) : OperationsRepository {

    private val refreshOperationsListEvents = MutableSharedFlow<Unit>()
    private val refreshOperationEvents = MutableSharedFlow<Unit>()
    private val refreshIncomeListEvents = MutableSharedFlow<Unit>()
    private val refreshExpensesListEvents = MutableSharedFlow<Unit>()
    private val refreshMoneyBoxOperationsListEvents = MutableSharedFlow<Unit>()
    private val refreshDebtsOperationsListEvents = MutableSharedFlow<Unit>()
    private val refreshTotalIncomeEvents = MutableSharedFlow<Unit>()
    private val refreshTotalExpenseEvents = MutableSharedFlow<Unit>()
    private val refreshCategoryExpensesListEvents = MutableSharedFlow<Unit>()

    override fun getOperationsList(): Flow<List<Operation>> = flow {
        val list = dao.getOperationsList()
        val result = mapper.mapListDbModelToListOperations(list)
        emit(result)
        refreshOperationsListEvents.collect {
            val updatedList = dao.getOperationsList()
            val updatedResult = mapper.mapListDbModelToListOperations(updatedList)
            emit(updatedResult)
        }
    }

    override fun getOperation(id: Int): Flow<Operation> = flow {
        val dbModel = dao.getOperation(id)
        val result = mapper.mapDbModelToOperation(dbModel)
        emit(result)
        refreshOperationEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedResult = mapper.mapDbModelToOperation(updatedDbModel)
            emit(updatedResult)
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
    }

    override suspend fun removeAllOperations() {
        dao.removeAllOperations()
    }

    override fun getIncome(id: Int): Flow<Income> = flow {
        val dbModel = dao.getOperation(id)
        val income = mapper.mapDbModelToIncome(dbModel)
        emit(income)
    }

    override fun getExpense(id: Int): Flow<Expense> = flow {
        val dbModel = dao.getOperation(id)
        val expense = mapper.mapDbModelToExpense(dbModel)
        emit(expense)
    }

    override fun getMoneyBoxOperation(id: Int): Flow<MoneyBoxOperation> = flow {
        val dbModel = dao.getOperation(id)
        val moneyBoxOperation = mapper.mapDbModelToMoneyBoxOperation(dbModel)
        emit(moneyBoxOperation)
    }

    override fun getDebtOperation(id: Int): Flow<DebtOperation> = flow {
        val dbModel = dao.getOperation(id)
        val debtOperation = mapper.mapDbModelToDebtOperation(dbModel)
        emit(debtOperation)
    }

    override fun getIncomeListForMonth(): Flow<List<Income>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val list = dao.getOperationsListByType(TYPE_INCOME, beginOfMonthMillis)
        val result = mapper.mapListDbModelToListIncome(list)
        emit(result)
        refreshIncomeListEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedList = dao.getOperationsListByType(TYPE_INCOME, updatedBeginOfMonthMillis)
            val updatedResult = mapper.mapListDbModelToListIncome(updatedList)
            emit(updatedResult)
        }
    }

    override fun getExpensesListForMonth(): Flow<List<Expense>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val list = dao.getOperationsListByType(TYPE_EXPENSE, beginOfMonthMillis)
        val result = mapper.mapListDbModelToListExpenses(list)
        emit(result)
        refreshExpensesListEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedList = dao.getOperationsListByType(TYPE_EXPENSE, updatedBeginOfMonthMillis)
            val updatedResult = mapper.mapListDbModelToListExpenses(updatedList)
            emit(updatedResult)
        }
    }

    override fun getMoneyBoxOperationsListFromDate(
        date: Timestamp
    ): Flow<List<MoneyBoxOperation>> = flow {
        val list = dao.getOperationsListByOperationForm(
            OPERATION_FORM_MONEY_BOX_OPERATION,
            date.time
        )
        val result = mapper.mapListDbModelToListMoneyBoxOperations(list)
        emit(result)
        refreshMoneyBoxOperationsListEvents.collect {
            val updatedList = dao.getOperationsListByOperationForm(
                OPERATION_FORM_MONEY_BOX_OPERATION,
                date.time
            )
            val updatedResult = mapper.mapListDbModelToListMoneyBoxOperations(updatedList)
            emit(updatedResult)
        }
    }

    override fun getDebtOperationsList(): Flow<List<DebtOperation>> = flow {
        val list = dao.getOperationsListByOperationForm(OPERATION_FORM_DEBT)
        val result = mapper.mapListDbModelToListDebtOperations(list)
        emit(result)
        refreshDebtsOperationsListEvents.collect {
            val updatedList = dao.getOperationsListByOperationForm(OPERATION_FORM_DEBT)
            val updatedResult = mapper.mapListDbModelToListDebtOperations(updatedList)
            emit(updatedResult)
        }
    }

    override fun getTotalIncomeAmountForMonth(): Flow<Int> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val result = dao.getOperationsAmountByType(
            TYPE_INCOME,
            beginOfMonthMillis
        ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
        emit(result)
        refreshTotalIncomeEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedResult = dao.getOperationsAmountByType(
                TYPE_INCOME,
                updatedBeginOfMonthMillis
            ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
            emit(updatedResult)
        }
    }

    override fun getTotalExpensesAmountForMonth(): Flow<Int> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val result = dao.getOperationsAmountByType(
            TYPE_EXPENSE,
            beginOfMonthMillis
        ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
        emit(result)
        refreshTotalExpenseEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedResult = dao.getOperationsAmountByType(
                TYPE_EXPENSE,
                updatedBeginOfMonthMillis
            ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
            emit(updatedResult)
        }
    }

    override fun getCategoryExpensesListForMonth(categoryName: String): Flow<List<Expense>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val list = dao.getCategoryExpensesList(categoryName, beginOfMonthMillis)
        val result = mapper.mapListDbModelToListExpenses(list)
        emit(result)
        refreshCategoryExpensesListEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedList = dao.getCategoryExpensesList(categoryName, updatedBeginOfMonthMillis)
            val updatedResult = mapper.mapListDbModelToListExpenses(updatedList)
            emit(updatedResult)
        }
    }

    override suspend fun refreshOperationData() {
        refreshOperationsListEvents.emit(Unit)
        refreshOperationEvents.emit(Unit)
        refreshIncomeListEvents.emit(Unit)
        refreshExpensesListEvents.emit(Unit)
        refreshMoneyBoxOperationsListEvents.emit(Unit)
        refreshDebtsOperationsListEvents.emit(Unit)
        refreshTotalIncomeEvents.emit(Unit)
        refreshTotalExpenseEvents.emit(Unit)
        refreshCategoryExpensesListEvents.emit(Unit)
    }

    companion object {
        private const val OPERATIONS_AMOUNT_DEFAULT_VALUE = 0
    }
}
