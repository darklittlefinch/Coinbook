package com.elliemoritz.coinbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.OperationDbModel

@Dao
interface OperationsDao {

    @Query("SELECT * FROM operations")
    fun getOperationsList(): LiveData<List<OperationDbModel>>

    @Query("SELECT * FROM operations " +
            "WHERE operationForm = :operationForm AND dateTimeMillis >= :dateMillis")
    fun getOperationFormList(
        operationForm: String,
        dateMillis: Long = 0)
    : LiveData<List<OperationDbModel>>

    @Query("SELECT * FROM operations WHERE info = :categoryName AND dateTimeMillis >= :dateMillis")
    fun getCategoryExpensesList(
        categoryName: String,
        dateMillis: Long
    ): LiveData<List<OperationDbModel>>

    @Query("SELECT * FROM operations WHERE id=:operationId LIMIT 1")
    suspend fun getOperation(operationId: Int): OperationDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOperation(operation: OperationDbModel)

    @Query("DELETE FROM operations WHERE id = :operationId")
    suspend fun removeOperation(operationId: Int)
}
