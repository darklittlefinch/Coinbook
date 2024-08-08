package com.elliemoritz.coinbook.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.AlarmDbModel

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms ORDER BY dateTimeMillis")
    suspend fun getAlarmsList(): List<AlarmDbModel>

    @Query("SELECT * FROM alarms WHERE id = :id LIMIT 1")
    suspend fun getAlarm(id: Long): AlarmDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: AlarmDbModel)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun removeAlarm(id: Long)

    @Query("SELECT COUNT(*) FROM alarms")
    suspend fun getAlarmsCount(): Long
}
