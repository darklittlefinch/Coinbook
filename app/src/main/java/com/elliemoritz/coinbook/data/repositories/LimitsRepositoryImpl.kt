package com.elliemoritz.coinbook.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.elliemoritz.coinbook.data.dao.LimitsDao
import com.elliemoritz.coinbook.data.mappers.LimitMapper
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository

class LimitsRepositoryImpl(
    private val dao: LimitsDao,
    private val mapper: LimitMapper
) : LimitsRepository {

    override fun getLimitsList(): LiveData<List<Limit>> {
        val limitsLiveData = dao.getLimitsList()
        return limitsLiveData.map {
            mapper.mapListDbModelToListEntities(it)
        }
    }

    override fun getLimit(id: Int): Limit {
        val dbModel = dao.getLimit(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun addLimit(limit: Limit) {
        val dbModel = mapper.mapEntityToDbModel(limit)
        dao.addLimit(dbModel)
    }

    override fun editLimit(limit: Limit) {
        val dbModel = mapper.mapEntityToDbModel(limit)
        dao.addLimit(dbModel)
    }

    override fun removeLimit(limit: Limit) {
        dao.removeLimit(limit.id)
    }
}