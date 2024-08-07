package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.LimitDbModel

@Dao
interface LimitsDao {

    @Query("SELECT * FROM limits")
    suspend fun getLimitsList(): List<LimitDbModel>

    @Query("SELECT * FROM limits WHERE id = :id LIMIT 1")
    suspend fun getLimit(id: Int): LimitDbModel

    @Query("SELECT * FROM limits WHERE categoryId = :categoryId")
    suspend fun getLimitByCategoryId(categoryId: Int): LimitDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLimit(limit: LimitDbModel)

    @Query("DELETE FROM limits WHERE id = :id")
    suspend fun removeLimit(id: Int)

    @Query("SELECT COUNT(*) FROM limits")
    suspend fun getLimitsCount(): Int
}
