package com.elliemoritz.coinbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.DebtDbModel

@Dao
interface DebtsDao {

    @Query("SELECT * FROM debts")
    fun getDebtsList(): LiveData<List<DebtDbModel>>

    @Query("SELECT * FROM debts WHERE id=:debtId LIMIT 1")
    suspend fun getDebt(debtId: Int): DebtDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebt(debt: DebtDbModel)

    @Query("DELETE FROM debts WHERE id=:debtId")
    suspend fun removeDebt(debtId: Int)
}
