package com.elliemoritz.coinbook.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.elliemoritz.coinbook.data.dao.DebtsDao
import com.elliemoritz.coinbook.data.mappers.DebtMapper
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository

class DebtsRepositoryImpl(
    private val dao: DebtsDao,
    private val mapper: DebtMapper
) : DebtsRepository {

    override fun getDebtsList(): LiveData<List<Debt>> {
        val debtsLiveData = dao.getDebtsList()
        return debtsLiveData.map {
            mapper.mapListDbModelToListEntities(it)
        }
    }

    override fun getDebt(id: Int): Debt {
        val dbModel = dao.getDebt(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun addDebt(debt: Debt) {
        val dbModel = mapper.mapEntityToDbModel(debt)
        dao.addDebt(dbModel)
    }

    override fun editDebt(debt: Debt) {
        val dbModel = mapper.mapEntityToDbModel(debt)
        dao.addDebt(dbModel)
    }

    override fun removeDebt(debt: Debt) {
        dao.removeDebt(debt.id)
    }
}