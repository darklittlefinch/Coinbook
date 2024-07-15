package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.MoneyBoxDao
import com.elliemoritz.coinbook.data.mappers.MoneyBoxMapper
import com.elliemoritz.coinbook.domain.entities.MoneyBox
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository

class MoneyBoxRepositoryImpl(
    private val dao: MoneyBoxDao,
    private val mapper: MoneyBoxMapper
) : MoneyBoxRepository {

    override fun getMoneyBox(id: Int): MoneyBox {
        val dbModel = dao.getMoneyBox(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun addMoneyBox(moneyBox: MoneyBox) {
        val dbModel = mapper.mapEntityToDbModel(moneyBox)
        dao.addMoneyBox(dbModel)
    }

    override fun editMoneyBox(moneyBox: MoneyBox) {
        val dbModel = mapper.mapEntityToDbModel(moneyBox)
        dao.addMoneyBox(dbModel)
    }

    override fun removeMoneyBox(moneyBox: MoneyBox) {
        dao.removeMoneyBox(moneyBox.id)
    }
}