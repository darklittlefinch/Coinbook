package com.elliemoritz.coinbook.di

import android.app.Application
import com.elliemoritz.coinbook.presentation.activities.MainActivity
import com.elliemoritz.coinbook.presentation.activities.SettingsActivity
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
    fun inject(activity: SettingsActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}