package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.LimitsDao
import com.elliemoritz.coinbook.data.mappers.LimitMapper
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LimitsRepositoryImpl @Inject constructor(
    private val dao: LimitsDao,
    private val mapper: LimitMapper
) : LimitsRepository {

    private val refreshListEvents = MutableSharedFlow<Unit>()
    private val refreshLimitEvents = MutableSharedFlow<Unit>()
    private val refreshCountEvents = MutableSharedFlow<Unit>()

    override fun getLimitsList(): Flow<List<Limit>> = flow {
        val list = dao.getLimitsList()
        val result = mapper.mapListDbModelToListEntities(list)
        emit(result)
        refreshListEvents.collect {
            val updatedList = dao.getLimitsList()
            val updatedResult = mapper.mapListDbModelToListEntities(updatedList)
            emit(updatedResult)
        }
    }

    override fun getLimit(id: Int): Flow<Limit> = flow {
        val dbModel = dao.getLimit(id)
        val result = mapper.mapDbModelToEntity(dbModel)
        emit(result)
        refreshLimitEvents.collect {
            val updatedDbModel = dao.getLimit(id)
            val updatedResult = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedResult)
        }
    }

    override suspend fun addLimit(limit: Limit) {
        val dbModel = mapper.mapEntityToDbModel(limit)
        dao.addLimit(dbModel)
    }

    override suspend fun editLimit(limit: Limit) {
        val dbModel = mapper.mapEntityToDbModel(limit)
        dao.addLimit(dbModel)
    }

    override suspend fun removeLimit(limit: Limit) {
        dao.removeLimit(limit.id)
    }

    override fun getLimitsCount(): Flow<Int> = flow {
        val result = dao.getLimitsCount()
        emit(result)
        refreshCountEvents.collect {
            val updatedResult = dao.getLimitsCount()
            emit(updatedResult)
        }
    }

    override suspend fun refreshLimitsData() {
        refreshListEvents.emit(Unit)
        refreshLimitEvents.emit(Unit)
        refreshCountEvents.emit(Unit)
    }
}