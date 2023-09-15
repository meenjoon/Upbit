package com.mbj.upbit

import android.app.Application
import com.mbj.upbit.data.remote.network.AppContainer

class CoinApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }

    companion object {
        lateinit var appContainer: AppContainer
    }
}
