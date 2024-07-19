package com.elliemoritz.coinbook.di

import androidx.lifecycle.ViewModel
import com.elliemoritz.coinbook.presentation.viewModels.MainViewModel
import com.elliemoritz.coinbook.presentation.viewModels.SettingsViewModel
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
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}