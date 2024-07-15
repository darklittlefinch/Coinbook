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
    fun getAlarm(alarmId: Int): AlarmDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAlarm(alarm: AlarmDbModel)

    @Query("DELETE FROM alarms WHERE id=:alarmId")
    fun removeAlarm(alarmId: AlarmDbModel)
}
