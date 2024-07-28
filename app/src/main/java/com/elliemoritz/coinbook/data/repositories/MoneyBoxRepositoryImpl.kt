package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.MoneyBoxDao
import com.elliemoritz.coinbook.data.mappers.MoneyBoxMapper
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoneyBoxRepositoryImpl @Inject constructor(
    private val dao: MoneyBoxDao,
    private val mapper: MoneyBoxMapper
) : MoneyBoxRepository {

    override fun getMoneyBox(): Flow<MoneyBox?> = flow {
        val dbModel = dao.getMoneyBox(MoneyBox.MONEY_BOX_ID)
        val moneyBox = if (dbModel != null) {
            mapper.mapDbModelToEntity(dbModel)
        } else {
            null
        }
        emit(moneyBox)
    }

    override suspend fun addMoneyBox(moneyBox: MoneyBox) {
        val dbModel = mapper.mapEntityToDbModel(moneyBox)
        dao.addMoneyBox(dbModel)
    }

    override suspend fun addToMoneyBox(amount: Int) {
        val dbModel = dao.getMoneyBox(MoneyBox.MONEY_BOX_ID) ?: return
        val newAmount = dbModel.totalAmount + amount
        val newDbModel = dbModel.copy(totalAmount = newAmount)
        dao.addMoneyBox(newDbModel)
    }

    override suspend fun removeFromMoneyBox(amount: Int) {
        val dbModel = dao.getMoneyBox(MoneyBox.MONEY_BOX_ID) ?: return
        val newAmount = dbModel.totalAmount - amount
        val newDbModel = dbModel.copy(totalAmount = newAmount)
        dao.addMoneyBox(newDbModel)
    }

    override suspend fun editMoneyBox(moneyBox: MoneyBox) {
        val dbModel = mapper.mapEntityToDbModel(moneyBox)
        dao.addMoneyBox(dbModel)
    }

    override suspend fun removeMoneyBox(moneyBox: MoneyBox) {
        dao.removeMoneyBox(moneyBox.id)
    }
}