package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.MoneyBoxOperationsDao
import com.elliemoritz.coinbook.data.mappers.MoneyBoxOperationsMapper
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxOperationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoneyBoxOperationsRepositoryImpl @Inject constructor(
    private val dao: MoneyBoxOperationsDao,
    private val mapper: MoneyBoxOperationsMapper
) : MoneyBoxOperationsRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getOperationsList(): Flow<List<MoneyBoxOperation>> = flow {
        val dbModelsList = dao.getOperationsList()
        val operationsList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(operationsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getOperationsList()
            val updatedOperationsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedOperationsList)
        }
    }

    override fun getOperationsListFromDate(dateTimeMillis: Long): Flow<List<MoneyBoxOperation>> =
        flow {
            val dbModelsList = dao.getOperationsListFromDate(dateTimeMillis)
            val operationsList = mapper.mapListDbModelToListEntities(dbModelsList)
            emit(operationsList)

            refreshEvents.collect {
                val updatedDbModelsList = dao.getOperationsListFromDate(dateTimeMillis)
                val updatedOperationsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
                emit(updatedOperationsList)
            }
        }

    override suspend fun removeAllOperations() {
        dao.removeAllOperations()
    }

    override fun getOperation(id: Long): Flow<MoneyBoxOperation> = flow {
        val dbModel = dao.getOperation(id)
        val operation = mapper.mapDbModelToEntity(dbModel)
        emit(operation)

        refreshEvents.collect {
            val updatedDbModel = dao.getOperation(id)
            val updatedOperation = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedOperation)
        }
    }

    override suspend fun addOperation(operation: MoneyBoxOperation) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun editOperation(operation: MoneyBoxOperation) {
        val dbModel = mapper.mapEntityToDbModel(operation)
        dao.addOperation(dbModel)
    }

    override suspend fun removeOperation(operation: MoneyBoxOperation) {
        dao.removeOperation(operation.id)
        refreshData()
    }
}
