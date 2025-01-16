package com.app.watchtime

import android.app.Application
import com.app.watchtime.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WatchTimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {

            androidLogger()
            androidContext(this@WatchTimeApplication)
            // Load modules
            modules(appModule)
        }
    }
}