package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.operations.DebtOperationDbModel

@Dao
interface DebtsOperationsDao {

    @Query("SELECT * FROM debt_operations ORDER BY dateTimeMillis DESC")
    suspend fun getOperationsList(): List<DebtOperationDbModel>

    @Query("DELETE FROM debt_operations")
    suspend fun removeAllOperations()

    @Query("SELECT * FROM debt_operations WHERE id = :id LIMIT 1")
    suspend fun getOperation(id: Long): DebtOperationDbModel

    @Query("SELECT * FROM debt_operations WHERE debtId = :debtId LIMIT 1")
    suspend fun getOperationByDebtId(debtId: Long): DebtOperationDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOperation(operation: DebtOperationDbModel)

    @Query("DELETE FROM debt_operations WHERE id = :id")
    suspend fun removeOperation(id: Long)
}
