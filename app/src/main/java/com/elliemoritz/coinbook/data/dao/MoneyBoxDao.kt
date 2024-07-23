package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel

@Dao
interface MoneyBoxDao {

    @Query("SELECT * FROM money_box WHERE id=:moneyBoxId LIMIT 1")
    suspend fun getMoneyBox(moneyBoxId: Int): MoneyBoxDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoneyBox(moneyBox: MoneyBoxDbModel)

    @Query("DELETE FROM money_box WHERE id=:moneyBoxId")
    suspend fun removeMoneyBox(moneyBoxId: Int)
}
