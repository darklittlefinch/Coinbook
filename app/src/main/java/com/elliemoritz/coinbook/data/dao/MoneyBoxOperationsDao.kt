package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.operations.MoneyBoxOperationDbModel

@Dao
interface MoneyBoxOperationsDao {

    @Query("SELECT * FROM mb_operations ORDER BY dateTimeMillis DESC")
    suspend fun getOperationsList(): List<MoneyBoxOperationDbModel>

    @Query(
        "SELECT * FROM mb_operations " +
                "WHERE dateTimeMillis >= :dateTimeMillis " +
                "ORDER BY dateTimeMillis DESC"
    )
    suspend fun getOperationsListFromDate(dateTimeMillis: Long): List<MoneyBoxOperationDbModel>

    @Query("DELETE FROM mb_operations")
    suspend fun removeAllOperations()

    @Query("SELECT * FROM mb_operations WHERE id = :id LIMIT 1")
    suspend fun getOperation(id: Int): MoneyBoxOperationDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOperation(operation: MoneyBoxOperationDbModel)

    @Query("DELETE FROM mb_operations WHERE id = :id")
    suspend fun removeOperation(id: Int)
}
