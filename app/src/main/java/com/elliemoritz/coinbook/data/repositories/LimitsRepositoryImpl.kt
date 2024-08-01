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

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getLimitsList(): Flow<List<Limit>> = flow {
        val dbModelsList = dao.getLimitsList()
        val limitsList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(limitsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getLimitsList()
            val updatedLimitsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedLimitsList)
        }
    }

    override fun getLimit(id: Int): Flow<Limit> = flow {
        val dbModel = dao.getLimit(id)
        val limit = mapper.mapDbModelToEntity(dbModel)
        emit(limit)
    }

    override fun getLimitByCategoryId(categoryId: Int): Flow<Limit?> = flow {
        val dbModel = dao.getLimitByCategoryId(categoryId)
        val limit = if (dbModel != null) {
            mapper.mapDbModelToEntity(dbModel)
        } else {
            null
        }
        emit(limit)
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
        refreshData()
    }

    override fun getLimitsCount(): Flow<Int> = flow {
        val limitsCount = dao.getLimitsCount()
        emit(limitsCount)
    }
}