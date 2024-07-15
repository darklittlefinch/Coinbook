package com.elliemoritz.coinbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel

@Dao
interface MoneyBoxDao {

    @Query("SELECT * FROM money_box WHERE id=:moneyBoxId LIMIT 1")
    fun getMoneyBox(moneyBoxId: Int): MoneyBoxDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMoneyBox(moneyBox: MoneyBoxDbModel)

    @Query("DELETE FROM money_box WHERE id=:moneyBoxId")
    fun removeMoneyBox(moneyBoxId: Int)
}
