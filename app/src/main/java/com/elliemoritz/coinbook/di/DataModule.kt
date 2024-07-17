package com.elliemoritz.coinbook.di

import android.app.Application
import com.elliemoritz.coinbook.data.AppDatabase
import com.elliemoritz.coinbook.data.dao.AlarmsDao
import com.elliemoritz.coinbook.data.dao.DebtsDao
import com.elliemoritz.coinbook.data.dao.LimitsDao
import com.elliemoritz.coinbook.data.dao.MoneyBoxDao
import com.elliemoritz.coinbook.data.dao.OperationsDao
import com.elliemoritz.coinbook.data.repositories.AlarmsRepositoryImpl
import com.elliemoritz.coinbook.data.repositories.DebtsRepositoryImpl
import com.elliemoritz.coinbook.data.repositories.LimitsRepositoryImpl
import com.elliemoritz.coinbook.data.repositories.MoneyBoxRepositoryImpl
import com.elliemoritz.coinbook.data.repositories.OperationsRepositoryImpl
import com.elliemoritz.coinbook.data.repositories.UserPreferencesRepositoryImpl
import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import com.elliemoritz.coinbook.domain.repositories.DebtsRepository
import com.elliemoritz.coinbook.domain.repositories.LimitsRepository
import com.elliemoritz.coinbook.domain.repositories.MoneyBoxRepository
import com.elliemoritz.coinbook.domain.repositories.OperationsRepository
import com.elliemoritz.coinbook.domain.repositories.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindAlarmsRepository(impl: AlarmsRepositoryImpl): AlarmsRepository

    @ApplicationScope
    @Binds
    fun bindDebtsRepository(impl: DebtsRepositoryImpl): DebtsRepository

    @ApplicationScope
    @Binds
    fun bindLimitsRepository(impl: LimitsRepositoryImpl): LimitsRepository

    @ApplicationScope
    @Binds
    fun bindMoneyBoxRepository(impl: MoneyBoxRepositoryImpl): MoneyBoxRepository

    @ApplicationScope
    @Binds
    fun bindOperationsRepository(impl: OperationsRepositoryImpl): OperationsRepository

    @ApplicationScope
    @Binds
    fun bindUserPreferencesRepository(
        impl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideAlarmsDao(application: Application): AlarmsDao {
            return AppDatabase.getInstance(application).alarmsDao()
        }

        @ApplicationScope
        @Provides
        fun provideDebtsDao(application: Application): DebtsDao {
            return AppDatabase.getInstance(application).debtsDao()
        }

        @ApplicationScope
        @Provides
        fun provideLimitsDao(application: Application): LimitsDao {
            return AppDatabase.getInstance(application).limitsDao()
        }

        @ApplicationScope
        @Provides
        fun provideMoneyBoxDao(application: Application): MoneyBoxDao {
            return AppDatabase.getInstance(application).moneyBoxDao()
        }

        @ApplicationScope
        @Provides
        fun provideOperationsDao(application: Application): OperationsDao {
            return AppDatabase.getInstance(application).operationsDao()
        }
    }
}