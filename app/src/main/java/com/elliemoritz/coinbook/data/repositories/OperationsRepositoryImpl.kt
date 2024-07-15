package com.elliemoritz.coinbook.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.elliemoritz.coinbook.data.dao.OperationsDao
import com.elliemoritz.coinbook.data.mappers.OperationsMapper
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository

class OperationsRepositoryImpl(
    private val dao: OperationsDao,
    private val mapper: OperationsMapper
) : OperationsRepository {

    override fun getOperationsList(): LiveData<List<Operation>> {
        val operationsLiveData = dao.getOperationsList()
        return operationsLiveData.map {
            mapper.mapListDbModelToListOperations(it)
        }
    }

    override fun getOperation(id: Int): Operation {
        val dbModel = dao.getOperation(id)
        return mapper.mapDbModelToOperation(dbModel)
    }

    override fun addOperation(operation: Operation) {
        val dbModel = mapper.mapOperationToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override fun editOperation(operation: Operation) {
        val dbModel = mapper.mapOperationToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override fun removeOperation(operation: Operation) {
        dao.removeOperation(operation.id)
    }

    override fun getIncomeList(): LiveData<List<Income>> {
        val operationsList = dao.getOperationFormList(OperationsMapper.OPERATION_FORM_INCOME)
        return operationsList.map {
            mapper.mapListDbModelToListIncome(it)
        }
    }

    override fun getExpensesList(): LiveData<List<Expense>> {
        val operationsList = dao.getOperationFormList(OperationsMapper.OPERATION_FORM_EXPENSE)
        return operationsList.map {
            mapper.mapListDbModelToListExpenses(it)
        }
    }

    override fun getMoneyBoxOperationsList(): LiveData<List<MoneyBoxOperation>> {
        val operationsList = dao.getOperationFormList(
            OperationsMapper.OPERATION_FORM_MONEY_BOX_OPERATION
        )
        return operationsList.map {
            mapper.mapListDbModelToListMoneyBoxOperations(it)
        }
    }

    override fun getDebtOperationsList(): LiveData<List<DebtOperation>> {
        val operationsList = dao.getOperationFormList(OperationsMapper.OPERATION_FORM_DEBT)
        return operationsList.map {
            mapper.mapListDbModelToListDebtOperations(it)
        }
    }
}
