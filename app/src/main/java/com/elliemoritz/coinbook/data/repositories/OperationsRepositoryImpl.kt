package com.elliemoritz.coinbook.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.elliemoritz.coinbook.data.dao.OperationsDao
import com.elliemoritz.coinbook.data.mappers.OperationsMapper
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_DEBT
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_EXPENSE
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_INCOME
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_MONEY_BOX_OPERATION
import com.elliemoritz.coinbook.data.util.TYPE_EXPENSE
import com.elliemoritz.coinbook.data.util.TYPE_INCOME
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import java.sql.Timestamp
import javax.inject.Inject

class OperationsRepositoryImpl @Inject constructor(
    private val dao: OperationsDao,
    private val mapper: OperationsMapper
) : OperationsRepository {

    override fun getOperationsList(): LiveData<List<Operation>> {
        val operationsLiveData = dao.getOperationsList()
        return operationsLiveData.map {
            mapper.mapListDbModelToListOperations(it)
        }
    }

    override suspend fun getOperation(id: Int): Operation {
        val dbModel = dao.getOperation(id)
        return mapper.mapDbModelToOperation(dbModel)
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

    override fun getIncomeListFromDate(date: Timestamp): LiveData<List<Income>> {
        val operationsList = dao.getOperationFormList(OPERATION_FORM_INCOME, date.time)
        return operationsList.map {
            mapper.mapListDbModelToListIncome(it)
        }
    }

    override fun getExpensesListFromDate(date: Timestamp): LiveData<List<Expense>> {
        val operationsList = dao.getOperationFormList(OPERATION_FORM_EXPENSE, date.time)
        return operationsList.map {
            mapper.mapListDbModelToListExpenses(it)
        }
    }

    override fun getMoneyBoxOperationsList(): LiveData<List<MoneyBoxOperation>> {
        val operationsList = dao.getOperationFormList(
            OPERATION_FORM_MONEY_BOX_OPERATION
        )
        return operationsList.map {
            mapper.mapListDbModelToListMoneyBoxOperations(it)
        }
    }

    override fun getDebtOperationsList(): LiveData<List<DebtOperation>> {
        val operationsList = dao.getOperationFormList(OPERATION_FORM_DEBT)
        return operationsList.map {
            mapper.mapListDbModelToListDebtOperations(it)
        }
    }

    override suspend fun getTotalIncomeAmountFromDate(date: Timestamp): Int {
        return dao.getOperationsAmountByType(
            TYPE_INCOME,
            date.time
        ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
    }

    override suspend fun getTotalExpensesAmountFromDate(date: Timestamp): Int {
        return dao.getOperationsAmountByType(
            TYPE_EXPENSE,
            date.time
        ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
    }

    override suspend fun getTotalMoneyBoxAmountFromDate(dateFrom: Timestamp): Int {
        return dao.getOperationsAmountByOperationFormAndType(
            OPERATION_FORM_MONEY_BOX_OPERATION,
            TYPE_INCOME,
            dateFrom.time
        ) ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
    }

    override fun getCategoryExpensesListFromDate(
        categoryName: String,
        date: Timestamp
    ): LiveData<List<Expense>> {
        val expensesList = dao.getCategoryExpensesList(categoryName, date.time)
        return expensesList.map {
            mapper.mapListDbModelToListExpenses(it)
        }
    }

    companion object {
        private const val OPERATIONS_AMOUNT_DEFAULT_VALUE = 0
    }
}
