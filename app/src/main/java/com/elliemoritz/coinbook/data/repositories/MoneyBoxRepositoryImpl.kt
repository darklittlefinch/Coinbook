package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.MoneyBoxDao
import com.elliemoritz.coinbook.data.mappers.MoneyBoxMapper
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoneyBoxRepositoryImpl @Inject constructor(
    private val dao: MoneyBoxDao,
    private val mapper: MoneyBoxMapper
) : MoneyBoxRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    override fun getMoneyBox(): Flow<MoneyBox?> = flow {
        val dbModel = dao.getMoneyBox(MoneyBox.MONEY_BOX_ID)
        val result = if (dbModel != null) {
            mapper.mapDbModelToEntity(dbModel)
        } else {
            null
        }
        emit(result)
        refreshEvents.collect {
            val updatedDbModel = dao.getMoneyBox(MoneyBox.MONEY_BOX_ID)
            val updatedResult = if (updatedDbModel != null) {
                mapper.mapDbModelToEntity(updatedDbModel)
            } else {
                null
            }
            emit(updatedResult)
        }
    }

    override suspend fun addMoneyBox(moneyBox: MoneyBox) {
        val dbModel = mapper.mapEntityToDbModel(moneyBox)
        dao.addMoneyBox(dbModel)
    }

    override suspend fun editMoneyBox(moneyBox: MoneyBox) {
        val dbModel = mapper.mapEntityToDbModel(moneyBox)
        dao.addMoneyBox(dbModel)
    }

    override suspend fun removeMoneyBox(moneyBox: MoneyBox) {
        dao.removeMoneyBox(moneyBox.id)
    }

    override suspend fun refreshMoneyBox() {
        refreshEvents.emit(Unit)
    }
}