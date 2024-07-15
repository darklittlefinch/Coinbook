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

    @Query("SELECT * FROM operations WHERE id=:operationId LIMIT 1")
    fun getOperation(operationId: Int): OperationDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOperation(operation: OperationDbModel)

    @Query("DELETE FROM operations WHERE id=:operationId")
    fun removeOperation(operationId: Int)
}
