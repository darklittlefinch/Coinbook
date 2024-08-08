package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.IncomeDao
import com.elliemoritz.coinbook.data.mappers.IncomeMapper
import com.elliemoritz.coinbook.data.util.getBeginOfMonthMillis
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.repositories.IncomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IncomeRepositoryImpl @Inject constructor(
    private val dao: IncomeDao,
    private val mapper: IncomeMapper
) : IncomeRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getIncomeList(): Flow<List<Income>> = flow {
        val dbModelsList = dao.getOperationsList()
        val operationsList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(operationsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getOperationsList()
            val updatedOperationsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedOperationsList)
        }
    }

    override fun getIncomeListForMonth(): Flow<List<Income>> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val dbModelsList = dao.getOperationsListFromDate(beginOfMonthMillis)
        val incomeList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(incomeList)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedDbModelsList = dao.getOperationsListFromDate(updatedBeginOfMonthMillis)
            val updatedIncomeList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedIncomeList)
        }
    }

    override suspend fun removeAllIncome() {
        dao.removeAllOperations()
    }

    override fun getIncome(id: Long): Flow<Income> = flow {
        val dbModel = dao.getOperation(id)
        val income = mapper.mapDbModelToEntity(dbModel)
        emit(income)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedIncome = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedIncome)
        }
    }

    override suspend fun addIncome(operation: Income) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun editIncome(operation: Income) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun removeIncome(operation: Income) {
        dao.removeOperation(operation.id)
        refreshData()
    }

    override fun getTotalIncomeAmountForMonth(): Flow<Int> = flow {
        val beginOfMonthMillis = getBeginOfMonthMillis()
        val incomeAmount = dao.getOperationsAmountFromDate(beginOfMonthMillis)
            ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
        emit(incomeAmount)

        refreshEvents.collect {
            val updatedBeginOfMonthMillis = getBeginOfMonthMillis()
            val updatedIncomeAmount = dao.getOperationsAmountFromDate(updatedBeginOfMonthMillis)
                ?: OPERATIONS_AMOUNT_DEFAULT_VALUE
            emit(updatedIncomeAmount)
        }
    }

    companion object {
        private const val OPERATIONS_AMOUNT_DEFAULT_VALUE = 0
    }
}
