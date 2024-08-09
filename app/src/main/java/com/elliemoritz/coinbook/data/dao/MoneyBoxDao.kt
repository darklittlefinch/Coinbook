package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel

@Dao
interface MoneyBoxDao {

    @Query("SELECT * FROM money_box WHERE id = :id LIMIT 1")
    suspend fun getMoneyBox(id: Long): MoneyBoxDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoneyBox(moneyBox: MoneyBoxDbModel)

    @Query("DELETE FROM money_box WHERE id = :id")
    suspend fun removeMoneyBox(id: Long)
}
