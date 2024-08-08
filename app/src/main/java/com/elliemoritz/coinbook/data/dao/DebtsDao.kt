package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.DebtDbModel

@Dao
interface DebtsDao {

    @Query("SELECT * FROM debts ORDER BY startedMillis DESC")
    suspend fun getDebtsList(): List<DebtDbModel>

    @Query("SELECT * FROM debts WHERE id = :id LIMIT 1")
    suspend fun getDebt(id: Int): DebtDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDebt(debt: DebtDbModel)

    @Query("DELETE FROM debts WHERE id = :id")
    suspend fun removeDebt(id: Int)

    @Query("SELECT SUM(amount) FROM debts WHERE finished = :finished")
    suspend fun getDebtsAmount(finished: Boolean): Int?
}
