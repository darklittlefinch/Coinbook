package com.elliemoritz.coinbook.presentation

import android.app.Application
import com.elliemoritz.coinbook.di.DaggerApplicationComponent

class CoinBookApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}