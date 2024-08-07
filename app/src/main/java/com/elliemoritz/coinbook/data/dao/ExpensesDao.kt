package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.operations.ExpenseDbModel

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM expenses ORDER BY dateTimeMillis DESC")
    suspend fun getOperationsList(): List<ExpenseDbModel>

    @Query(
        "SELECT * FROM expenses " +
                "WHERE categoryId = :categoryId " +
                "AND dateTimeMillis >= :dateMillis " +
                "ORDER BY dateTimeMillis DESC"
    )
    suspend fun getCategoryExpensesListFromDate(
        categoryId: Int,
        dateMillis: Long
    ): List<ExpenseDbModel>

    @Query(
        "SELECT * FROM expenses " +
                "WHERE dateTimeMillis >= :dateTimeMillis " +
                "ORDER BY dateTimeMillis DESC"
    )
    suspend fun getOperationsListFromDate(dateTimeMillis: Long): List<ExpenseDbModel>

    @Query(
        "SELECT SUM(amount) FROM expenses " +
                "WHERE dateTimeMillis >= :dateTimeMillis"
    )
    suspend fun getOperationsAmountFromDate(dateTimeMillis: Long): Int?

    @Query(
        "SELECT SUM(amount) FROM expenses " +
                "WHERE categoryId = :categoryId " +
                "AND dateTimeMillis >= :dateTimeMillis"
    )
    suspend fun getOperationsAmountByCategoryFromDate(
        categoryId: Int,
        dateTimeMillis: Long
    ): Int?

    @Query("DELETE FROM expenses")
    suspend fun removeAllOperations()

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    suspend fun getOperation(id: Int): ExpenseDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOperation(operation: ExpenseDbModel)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun removeOperation(id: Int)
}
