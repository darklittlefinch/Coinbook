package com.elliemoritz.coinbook.di

import android.app.Application
import com.elliemoritz.coinbook.presentation.activities.AlarmsActivity
import com.elliemoritz.coinbook.presentation.activities.DebtsActivity
import com.elliemoritz.coinbook.presentation.activities.ExpensesActivity
import com.elliemoritz.coinbook.presentation.activities.HistoryActivity
import com.elliemoritz.coinbook.presentation.activities.IncomeActivity
import com.elliemoritz.coinbook.presentation.activities.LimitsActivity
import com.elliemoritz.coinbook.presentation.activities.MainActivity
import com.elliemoritz.coinbook.presentation.activities.MoneyBoxActivity
import com.elliemoritz.coinbook.presentation.activities.OperationsActivity
import com.elliemoritz.coinbook.presentation.activities.SettingsActivity
import com.elliemoritz.coinbook.presentation.fragments.AddAlarmFragment
import com.elliemoritz.coinbook.presentation.fragments.AddCategoryFragment
import com.elliemoritz.coinbook.presentation.fragments.AddDebtFragment
import com.elliemoritz.coinbook.presentation.fragments.AddExpenseFragment
import com.elliemoritz.coinbook.presentation.fragments.AddIncomeFragment
import com.elliemoritz.coinbook.presentation.fragments.AddLimitFragment
import com.elliemoritz.coinbook.presentation.fragments.AddMoneyBoxFragment
import com.elliemoritz.coinbook.presentation.fragments.AddMoneyBoxOperationFragment
import com.elliemoritz.coinbook.presentation.fragments.EditBalanceFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: IncomeActivity)
    fun inject(activity: ExpensesActivity)
    fun inject(activity: MoneyBoxActivity)
    fun inject(activity: DebtsActivity)
    fun inject(activity: LimitsActivity)
    fun inject(activity: AlarmsActivity)
    fun inject(activity: HistoryActivity)
    fun inject(activity: SettingsActivity)
    fun inject(activity: OperationsActivity)

    fun inject(fragment: EditBalanceFragment)
    fun inject(fragment: AddIncomeFragment)
    fun inject(fragment: AddExpenseFragment)
    fun inject(fragment: AddMoneyBoxFragment)
    fun inject(fragment: AddMoneyBoxOperationFragment)
    fun inject(fragment: AddDebtFragment)
    fun inject(fragment: AddLimitFragment)
    fun inject(fragment: AddAlarmFragment)
    fun inject(fragment: AddCategoryFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}