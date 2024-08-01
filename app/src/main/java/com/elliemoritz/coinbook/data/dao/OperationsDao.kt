package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.OperationDbModel

@Dao
interface OperationsDao {

    @Query("SELECT * FROM operations ORDER BY dateTimeMillis DESC")
    suspend fun getOperationsList(): List<OperationDbModel>

    @Query("SELECT * FROM operations WHERE id = :operationId LIMIT 1")
    suspend fun getOperation(operationId: Int): OperationDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOperation(operation: OperationDbModel)

    @Query("DELETE FROM operations WHERE id = :operationId")
    suspend fun removeOperation(operationId: Int)

    @Query("DELETE FROM operations")
    suspend fun removeAllOperations()

    @Query(
        "SELECT * FROM operations " +
                "WHERE operationForm = :operationForm"
    )
    suspend fun getOperationsListByOperationForm(
        operationForm: String
    )
            : List<OperationDbModel>

    @Query(
        "SELECT * FROM operations " +
                "WHERE type = :type " +
                "AND dateTimeMillis >= :dateMillis " +
                "ORDER BY dateTimeMillis DESC"
    )
    suspend fun getOperationsListByType(
        type: String,
        dateMillis: Long = 0
    ): List<OperationDbModel>

    @Query("SELECT * FROM operations " +
            "WHERE info = :categoryName " +
            "AND dateTimeMillis >= :dateMillis")
    suspend fun getCategoryExpensesList(
        categoryName: String,
        dateMillis: Long
    ): List<OperationDbModel>

    @Query(
        "SELECT SUM(amount) FROM operations " +
                "WHERE type = :type AND dateTimeMillis >= :dateMillis"
    )
    suspend fun getOperationsAmountByType(type: String, dateMillis: Long = 0): Int?

    @Query(
        "SELECT SUM(amount) FROM operations " +
                "WHERE operationForm = :operationForm " +
                "AND type = :type " +
                "AND dateTimeMillis >= :dateMillis"
    )
    suspend fun getOperationsAmountByOperationFormAndType(
        operationForm: String,
        type: String,
        dateMillis: Long
    ): Int?
}
