package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.operations.IncomeDbModel

@Dao
interface IncomeDao {

    @Query("SELECT * FROM income ORDER BY dateTimeMillis DESC")
    suspend fun getOperationsList(): List<IncomeDbModel>

    @Query(
        "SELECT * FROM income " +
                "WHERE dateTimeMillis >= :dateTimeMillis " +
                "ORDER BY dateTimeMillis DESC"
    )
    suspend fun getOperationsListFromDate(dateTimeMillis: Long): List<IncomeDbModel>

    @Query(
        "SELECT SUM(amount) FROM income " +
                "WHERE dateTimeMillis >= :dateTimeMillis"
    )
    suspend fun getOperationsAmountFromDate(dateTimeMillis: Long): Int?

    @Query("DELETE FROM income")
    suspend fun removeAllOperations()

    @Query("SELECT * FROM income WHERE id = :id LIMIT 1")
    suspend fun getOperation(id: Long): IncomeDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOperation(operation: IncomeDbModel)

    @Query("DELETE FROM income WHERE id = :id")
    suspend fun removeOperation(id: Long)
}
