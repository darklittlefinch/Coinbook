package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.DebtsOperationsDao
import com.elliemoritz.coinbook.data.dao.ExpensesDao
import com.elliemoritz.coinbook.data.dao.IncomeDao
import com.elliemoritz.coinbook.data.dao.MoneyBoxOperationsDao
import com.elliemoritz.coinbook.data.mappers.OperationsMapper
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OperationsRepositoryImpl @Inject constructor(
    private val incomeDao: IncomeDao,
    private val expensesDao: ExpensesDao,
    private val moneyBoxOperationsDao: MoneyBoxOperationsDao,
    private val debtsOperationsDao: DebtsOperationsDao,
    private val mapper: OperationsMapper
) : OperationsRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getAllOperationsList(): Flow<List<Operation>> = flow {

        val operationsList = mapper.mapListsOperationsToOneList(
            incomeDao.getOperationsList(),
            expensesDao.getOperationsList(),
            moneyBoxOperationsDao.getOperationsList(),
            debtsOperationsDao.getOperationsList()
        ).sortedByDescending { it.dateTimeMillis }
        emit(operationsList)

        refreshEvents.collect {

            val updatedOperationsList = mapper.mapListsOperationsToOneList(
                incomeDao.getOperationsList(),
                expensesDao.getOperationsList(),
                moneyBoxOperationsDao.getOperationsList(),
                debtsOperationsDao.getOperationsList()
            ).sortedByDescending { it.dateTimeMillis }
            emit(updatedOperationsList)
        }
    }

    override suspend fun removeAllOperations() {
        incomeDao.removeAllOperations()
        expensesDao.removeAllOperations()
        moneyBoxOperationsDao.removeAllOperations()
        debtsOperationsDao.removeAllOperations()
        refreshData()
    }
}
