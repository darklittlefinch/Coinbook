package com.elliemoritz.coinbook.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elliemoritz.coinbook.data.dao.AlarmsDao
import com.elliemoritz.coinbook.data.dao.DebtsDao
import com.elliemoritz.coinbook.data.dao.ExpenseCategoriesDao
import com.elliemoritz.coinbook.data.dao.LimitsDao
import com.elliemoritz.coinbook.data.dao.MoneyBoxDao
import com.elliemoritz.coinbook.data.dao.OperationsDao
import com.elliemoritz.coinbook.data.dbModels.AlarmDbModel
import com.elliemoritz.coinbook.data.dbModels.DebtDbModel
import com.elliemoritz.coinbook.data.dbModels.ExpenseCategoryDbModel
import com.elliemoritz.coinbook.data.dbModels.LimitDbModel
import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel
import com.elliemoritz.coinbook.data.dbModels.OperationDbModel

@Database(
    entities = [
        AlarmDbModel::class,
        DebtDbModel::class,
        ExpenseCategoryDbModel::class,
        LimitDbModel::class,
        MoneyBoxDbModel::class,
        OperationDbModel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alarmsDao(): AlarmsDao
    abstract fun debtsDao(): DebtsDao
    abstract fun expenseCategoriesDao(): ExpenseCategoriesDao
    abstract fun limitsDao(): LimitsDao
    abstract fun moneyBoxDao(): MoneyBoxDao
    abstract fun operationsDao(): OperationsDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "coin_book.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }

                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()

                INSTANCE = db
                return db
            }
        }
    }
}
