package com.elliemoritz.coinbook.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elliemoritz.coinbook.data.dao.AlarmsDao
import com.elliemoritz.coinbook.data.dao.CategoriesDao
import com.elliemoritz.coinbook.data.dao.DebtsDao
import com.elliemoritz.coinbook.data.dao.DebtsOperationsDao
import com.elliemoritz.coinbook.data.dao.ExpensesDao
import com.elliemoritz.coinbook.data.dao.IncomeDao
import com.elliemoritz.coinbook.data.dao.LimitsDao
import com.elliemoritz.coinbook.data.dao.MoneyBoxDao
import com.elliemoritz.coinbook.data.dao.MoneyBoxOperationsDao
import com.elliemoritz.coinbook.data.dbModels.AlarmDbModel
import com.elliemoritz.coinbook.data.dbModels.CategoryDbModel
import com.elliemoritz.coinbook.data.dbModels.DebtDbModel
import com.elliemoritz.coinbook.data.dbModels.LimitDbModel
import com.elliemoritz.coinbook.data.dbModels.MoneyBoxDbModel
import com.elliemoritz.coinbook.data.dbModels.operations.DebtOperationDbModel
import com.elliemoritz.coinbook.data.dbModels.operations.ExpenseDbModel
import com.elliemoritz.coinbook.data.dbModels.operations.IncomeDbModel
import com.elliemoritz.coinbook.data.dbModels.operations.MoneyBoxOperationDbModel

@Database(
    entities = [
        AlarmDbModel::class,
        DebtDbModel::class,
        CategoryDbModel::class,
        LimitDbModel::class,
        MoneyBoxDbModel::class,
        IncomeDbModel::class,
        ExpenseDbModel::class,
        MoneyBoxOperationDbModel::class,
        DebtOperationDbModel::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alarmsDao(): AlarmsDao
    abstract fun debtsDao(): DebtsDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun limitsDao(): LimitsDao
    abstract fun moneyBoxDao(): MoneyBoxDao
    abstract fun incomeDao(): IncomeDao
    abstract fun expensesDao(): ExpensesDao
    abstract fun moneyBoxOperationsDao(): MoneyBoxOperationsDao
    abstract fun debtsOperationsDao(): DebtsOperationsDao

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
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = db
                return db
            }
        }
    }
}
