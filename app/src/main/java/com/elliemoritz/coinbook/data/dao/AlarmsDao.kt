package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.AlarmDbModel

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms")
    suspend fun getAlarmsList(): List<AlarmDbModel>

    @Query("SELECT * FROM alarms WHERE id = :id LIMIT 1")
    suspend fun getAlarm(id: Int): AlarmDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: AlarmDbModel)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun removeAlarm(id: Int)

    @Query("SELECT COUNT(*) FROM alarms")
    suspend fun getAlarmsCount(): Int
}
