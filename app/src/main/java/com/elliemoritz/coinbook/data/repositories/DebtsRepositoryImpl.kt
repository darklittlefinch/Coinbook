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

    private val refreshListEvents = MutableSharedFlow<Unit>()
    private val refreshDebtEvents = MutableSharedFlow<Unit>()
    private val refreshTotalAmountEvents = MutableSharedFlow<Unit>()

    override fun getDebtsList(): Flow<List<Debt>> = flow {
        val list = dao.getDebtsList()
        val result = mapper.mapListDbModelToListEntities(list)
        emit(result)
        refreshListEvents.collect {
            val updatedList = dao.getDebtsList()
            val updatedResult = mapper.mapListDbModelToListEntities(updatedList)
            emit(updatedResult)
        }
    }

    override fun getDebt(id: Int): Flow<Debt> = flow {
        val dbModel = dao.getDebt(id)
        val result = mapper.mapDbModelToEntity(dbModel)
        emit(result)
        refreshDebtEvents.collect {
            val updatedDbModel = dao.getDebt(id)
            val updatedResult = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedResult)
        }
    }

    override suspend fun addDebt(debt: Debt) {
        val dbModel = mapper.mapEntityToDbModel(debt)
        dao.addDebt(dbModel)
    }

    override suspend fun editDebt(debt: Debt) {
        val dbModel = mapper.mapEntityToDbModel(debt)
        dao.addDebt(dbModel)
    }

    override suspend fun removeDebt(debt: Debt) {
        dao.removeDebt(debt.id)
    }

    override fun getTotalDebtsAmount(): Flow<Int> = flow {
        val totalAmount = dao.getDebtsAmount() ?: DEBTS_AMOUNT_DEFAULT_VALUE
        emit(totalAmount)
        refreshTotalAmountEvents.collect {
            val updatedTotalAmount = dao.getDebtsAmount() ?: DEBTS_AMOUNT_DEFAULT_VALUE
            emit(updatedTotalAmount)
        }
    }

    override suspend fun refreshDebtsData() {
        refreshListEvents.emit(Unit)
        refreshDebtEvents.emit(Unit)
        refreshTotalAmountEvents.emit(Unit)
    }

    companion object {
        private const val DEBTS_AMOUNT_DEFAULT_VALUE = 0
    }
}