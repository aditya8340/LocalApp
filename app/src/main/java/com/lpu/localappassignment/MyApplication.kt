package com.lpu.localappassignment

import android.app.Application
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Debug-only logging tree
        Timber.plant(Timber.DebugTree())
    }
}
