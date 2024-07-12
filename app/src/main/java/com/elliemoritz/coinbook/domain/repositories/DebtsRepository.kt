package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Debt

interface DebtsRepository {
    fun getDebtsList(): LiveData<List<Debt>>
    fun getDebt(id: Int): Debt
    fun addDebt(debt: Debt)
    fun editDebt(debt: Debt)
    fun removeDebt(debt: Debt)
}
