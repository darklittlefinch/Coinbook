package com.elliemoritz.coinbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.LimitDbModel

@Dao
interface LimitsDao {

    @Query("SELECT * FROM limits")
    fun getLimitsList(): LiveData<List<LimitDbModel>>

    @Query("SELECT * FROM limits WHERE id=:limitId LIMIT 1")
    fun getLimit(limitId: Int): LimitDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLimit(limit: LimitDbModel)

    @Query("DELETE FROM limits WHERE id=:limitId")
    fun removeLimit(limitId: LimitDbModel)
}
