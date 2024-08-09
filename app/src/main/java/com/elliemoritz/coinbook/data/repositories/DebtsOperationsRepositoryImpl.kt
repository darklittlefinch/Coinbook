package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.DebtsOperationsDao
import com.elliemoritz.coinbook.data.mappers.DebtOperationsMapper
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.repositories.DebtsOperationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DebtsOperationsRepositoryImpl @Inject constructor(
    private val dao: DebtsOperationsDao,
    private val mapper: DebtOperationsMapper
) : DebtsOperationsRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getOperationsList(): Flow<List<DebtOperation>> = flow {
        val dbModelsList = dao.getOperationsList()
        val operationsList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(operationsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getOperationsList()
            val updatedOperationsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedOperationsList)
        }
    }

    override suspend fun removeAllOperations() {
        dao.removeAllOperations()
    }

    override fun getOperation(id: Long): Flow<DebtOperation> = flow {
        val dbModel = dao.getOperation(id)
        val operation = mapper.mapDbModelToEntity(dbModel)
        emit(operation)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedOperation = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedOperation)
        }
    }

    override fun getOperationByDebtId(debtId: Long): Flow<DebtOperation> = flow {
        val dbModel = dao.getOperationByDebtId(debtId)
        val operation = mapper.mapDbModelToEntity(dbModel)
        emit(operation)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperationByDebtId(debtId)
            val updatedOperation = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedOperation)
        }
    }

    override suspend fun addOperation(operation: DebtOperation) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun editOperation(operation: DebtOperation) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun removeOperation(operation: DebtOperation) {
        dao.removeOperation(operation.id)
        refreshData()
    }
}
