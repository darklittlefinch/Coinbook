package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.DebtsDao
import com.elliemoritz.coinbook.data.mappers.DebtMapper
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DebtsRepositoryImpl @Inject constructor(
    private val dao: DebtsDao,
    private val mapper: DebtMapper
) : DebtsRepository {

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getDebtsList(): Flow<List<Debt>> = flow {
        val dbModelsList = dao.getDebtsList()
        val debtsList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(debtsList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getDebtsList()
            val updatedDebtsList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedDebtsList)
        }
    }

    override fun getDebt(id: Long): Flow<Debt> = flow {
        val dbModel = dao.getDebt(id)
        val debt = mapper.mapDbModelToEntity(dbModel)
        emit(debt)

        refreshEvents.collect {
            val updatedDbModel = dao.getDebt(id)
            val updatedDebt = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedDebt)
        }
    }

    override suspend fun addDebt(debt: Debt) {
        val dbModel = mapper.mapEntityToDbModel(debt)
        dao.addDebt(dbModel)
    }

    override suspend fun editDebt(debt: Debt) {
        val dbModel = mapper.mapEntityToDbModel(debt)
        dao.addDebt(dbModel)
        refreshData()
    }

    override suspend fun removeDebt(debt: Debt) {
        dao.removeDebt(debt.id)
        refreshData()
    }

    override fun getTotalDebtsAmount(finished: Boolean): Flow<Int> = flow {
        val totalAmount = dao.getDebtsAmount(finished) ?: DEBTS_AMOUNT_DEFAULT_VALUE
        emit(totalAmount)

        refreshEvents.collect {
            val updatedTotalAmount = dao.getDebtsAmount(finished) ?: DEBTS_AMOUNT_DEFAULT_VALUE
            emit(updatedTotalAmount)
        }
    }

    companion object {
        private const val DEBTS_AMOUNT_DEFAULT_VALUE = 0
    }
}