package com.elliemoritz.coinbook.di

import androidx.lifecycle.ViewModel
import com.elliemoritz.coinbook.presentation.viewModels.IncomeViewModel
import com.elliemoritz.coinbook.presentation.viewModels.MainViewModel
import com.elliemoritz.coinbook.presentation.viewModels.SettingsViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddCategoryViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddDebtViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddExpenseViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddIncomeViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddLimitViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddMoneyBoxOperationViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddMoneyBoxViewModel
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.EditBalanceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    fun bindIncomeViewModel(viewModel: IncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditBalanceViewModel::class)
    fun bindEditBalanceViewModel(viewModel: EditBalanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddIncomeViewModel::class)
    fun bindAddIncomeViewModel(viewModel: AddIncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddExpenseViewModel::class)
    fun bindAddExpenseViewModel(viewModel: AddExpenseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddMoneyBoxViewModel::class)
    fun bindAddMoneyBoxViewModel(viewModel: AddMoneyBoxViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddCategoryViewModel::class)
    fun bindAddCategoryViewModel(viewModel: AddCategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddMoneyBoxOperationViewModel::class)
    fun bindAddMoneyBoxOperationViewModel(viewModel: AddMoneyBoxOperationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddDebtViewModel::class)
    fun bindAddDebtViewModel(viewModel: AddDebtViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddLimitViewModel::class)
    fun bindAddLimitViewModel(viewModel: AddLimitViewModel): ViewModel
}