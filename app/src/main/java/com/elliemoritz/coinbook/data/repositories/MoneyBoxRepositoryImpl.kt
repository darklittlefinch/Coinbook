package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.MoneyBoxDao
import com.elliemoritz.coinbook.data.mappers.MoneyBoxMapper
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import javax.inject.Inject

class MoneyBoxRepositoryImpl @Inject constructor(
    private val dao: MoneyBoxDao,
    private val mapper: MoneyBoxMapper
) : MoneyBoxRepository {

    override suspend fun getMoneyBox(id: Int): MoneyBox? {
        val dbModel = dao.getMoneyBox(id) ?: return null
        return mapper.mapDbModelToEntity(dbModel)
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
}