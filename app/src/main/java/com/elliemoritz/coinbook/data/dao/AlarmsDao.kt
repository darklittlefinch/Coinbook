package com.elliemoritz.coinbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.AlarmDbModel

@Dao
interface AlarmsDao {

    @Query("SELECT * FROM alarms")
    fun getAlarmsList(): LiveData<List<AlarmDbModel>>

    @Query("SELECT * FROM alarms WHERE id=:alarmId LIMIT 1")
    suspend fun getAlarm(alarmId: Int): AlarmDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: AlarmDbModel)

    @Query("DELETE FROM alarms WHERE id=:alarmId")
    suspend fun removeAlarm(alarmId: Int)

    @Query("SELECT COUNT(*) FROM alarms")
    suspend fun getAlarmsCount(): Int
}
